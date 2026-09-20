package com.atguigu.pathology.service.impl;

import com.atguigu.pathology.dto.ChatRequest;
import com.atguigu.pathology.entity.ChatMessage;
import com.atguigu.pathology.mapper.ChatMessageMapper;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements com.atguigu.pathology.service.ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final ChatMessageMapper chatMessageMapper;
    private final WebClient webClient;

    @Value("${dashscope.api-key:}")
    private String apiKey;

    @Value("${dashscope.api-url:https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions}")
    private String apiUrl;

    @Value("${dashscope.model:qwen-plus}")
    private String model;

    private static final String SYSTEM_PROMPT = "你是一位专业的病理学专家助手。你的职责是为用户提供病理学相关的知识解答。回答时请使用清晰的结构、专业的术语，并在适当时给出临床意义。请确保回答内容准确、简洁。";

    @Autowired
    public ChatServiceImpl(ChatMessageMapper chatMessageMapper, WebClient.Builder webClientBuilder) {
        this.chatMessageMapper = chatMessageMapper;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Flux<String> chatStream(ChatRequest req) {
        String sessionId = req.getSessionId() != null && !req.getSessionId().isEmpty()
                ? req.getSessionId()
                : String.valueOf(System.currentTimeMillis());
        String query = req.getQuery();

        if (query == null || query.trim().isEmpty()) {
            return Flux.just("消息内容不能为空");
        }

        long sessionIdNum = parseSessionId(sessionId);

        // Store user message
        try {
            ChatMessage userMsg = new ChatMessage();
            userMsg.setSessionId(sessionIdNum);
            userMsg.setSender("user");
            userMsg.setContent(query);
            userMsg.setCreatedAt(LocalDateTime.now());
            chatMessageMapper.insert(userMsg);
        } catch (Exception e) {
            log.warn("存储用户消息失败: {}", e.getMessage());
        }

        // Get recent history (last 10 messages)
        List<ChatMessage> history = getRecentHistory(sessionIdNum, 10);

        // Build messages for DashScope API
        JSONArray messages = new JSONArray();

        // System message
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);
        messages.add(systemMsg);

        // History messages
        for (ChatMessage msg : history) {
            JSONObject msgObj = new JSONObject();
            msgObj.put("role", "user".equals(msg.getSender()) ? "user" : "assistant");
            msgObj.put("content", msg.getContent());
            messages.add(msgObj);
        }

        // Current user message
        JSONObject currentMsg = new JSONObject();
        currentMsg.put("role", "user");
        currentMsg.put("content", query);
        messages.add(currentMsg);

        // Build request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("stream", true);

        // Call DashScope streaming API
        List<String> collectedAnswer = Collections.synchronizedList(new ArrayList<>());

        return webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody.toJSONString())
                .retrieve()
                .bodyToFlux(String.class)
                .filter(line -> line.startsWith("data:"))
                .map(line -> line.substring(5).trim())
                .filter(data -> !"[DONE]".equals(data))
                .flatMap(data -> {
                    try {
                        JSONObject json = JSON.parseObject(data);
                        JSONArray choices = json.getJSONArray("choices");
                        if (choices != null && !choices.isEmpty()) {
                            JSONObject delta = choices.getJSONObject(0).getJSONObject("delta");
                            if (delta != null) {
                                String content = delta.getString("content");
                                if (content != null && !content.isEmpty()) {
                                    collectedAnswer.add(content);
                                    return Flux.just(content);
                                }
                            }
                        }
                        return Flux.empty();
                    } catch (Exception e) {
                        log.debug("解析流数据失败: {}", data);
                        return Flux.empty();
                    }
                })
                .doOnComplete(() -> {
                    // Store AI response
                    String answer = String.join("", collectedAnswer);
                    if (!answer.isEmpty()) {
                        try {
                            ChatMessage aiMsg = new ChatMessage();
                            aiMsg.setSessionId(sessionIdNum);
                            aiMsg.setSender("ai");
                            aiMsg.setContent(answer);
                            aiMsg.setCreatedAt(LocalDateTime.now());
                            chatMessageMapper.insert(aiMsg);
                            log.info("AI 回答已存储: {}", answer.substring(0, Math.min(50, answer.length())));
                        } catch (Exception e) {
                            log.warn("存储 AI 消息失败: {}", e.getMessage());
                        }
                    }
                })
                .doOnError(e -> log.error("调用 AI 服务失败: {}", e.getMessage()))
                .timeout(Duration.ofSeconds(120));
    }

    @Override
    public List<ChatMessage> getHistory(String sessionId) {
        try {
            long sessionIdNum = parseSessionId(sessionId);
            return getRecentHistory(sessionIdNum, 50);
        } catch (Exception e) {
            log.warn("获取历史记录失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void clearHistory(String sessionId) {
        try {
            long sessionIdNum = parseSessionId(sessionId);
            chatMessageMapper.delete(new QueryWrapper<ChatMessage>().eq("session_id", sessionIdNum));
            log.info("已清除会话历史: {}", sessionIdNum);
        } catch (Exception e) {
            log.warn("清除历史记录失败: {}", e.getMessage());
        }
    }

    private List<ChatMessage> getRecentHistory(long sessionId, int limit) {
        try {
            QueryWrapper<ChatMessage> wrapper = new QueryWrapper<>();
            wrapper.eq("session_id", sessionId)
                    .orderByDesc("created_at")
                    .last("LIMIT " + limit);
            List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
            // Reverse to get chronological order
            return messages.stream()
                    .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("获取最近历史失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private long parseSessionId(String sessionId) {
        try {
            String numericPart = sessionId.replaceAll("[^0-9]", "");
            return !numericPart.isEmpty() ? Long.parseLong(numericPart) : Math.abs(sessionId.hashCode());
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }
}
