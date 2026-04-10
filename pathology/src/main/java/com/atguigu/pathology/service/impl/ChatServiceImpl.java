package com.atguigu.pathology.service.impl;

import com.atguigu.pathology.dto.ChatRequest;
import com.atguigu.pathology.dto.ChatResponse;
import com.atguigu.pathology.entity.ChatMessage;
import com.atguigu.pathology.entity.KnowledgeArticle;
import com.atguigu.pathology.mapper.ChatMessageMapper;
import com.atguigu.pathology.mapper.KnowledgeArticleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ChatServiceImpl implements com.atguigu.pathology.service.ChatService {

    private final KnowledgeArticleMapper knowledgeArticleMapper;
    private final ChatMessageMapper chatMessageMapper;

    public ChatServiceImpl(KnowledgeArticleMapper knowledgeArticleMapper, ChatMessageMapper chatMessageMapper) {
        this.knowledgeArticleMapper = knowledgeArticleMapper;
        this.chatMessageMapper = chatMessageMapper;
    }

    @Override
    public ChatResponse chat(ChatRequest req) {
<<<<<<< HEAD
        Long sessionId = req.getSessionId() != null ? req.getSessionId() : 1L;
=======
        // 处理 sessionId，确保生成一个有效的数字
        String sessionIdStr = req.getSessionId() != null && !req.getSessionId().isEmpty() ? req.getSessionId() : "1";
        // 提取数字部分或使用哈希值
        long sessionId;
        try {
            // 尝试从 sessionId 中提取数字
            String numericPart = sessionIdStr.replaceAll("[^0-9]", "");
            if (!numericPart.isEmpty()) {
                sessionId = Long.parseLong(numericPart);
            } else {
                // 如果没有数字，使用哈希值
                sessionId = Math.abs(sessionIdStr.hashCode());
            }
        } catch (Exception e) {
            // 兜底方案
            sessionId = 1L;
        }
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d

        // store user message
        ChatMessage userMsg = new ChatMessage();
        userMsg.setSessionId(sessionId);
        userMsg.setSender("user");
        userMsg.setContent(req.getQuery());
        userMsg.setCreatedAt(LocalDateTime.now());
        chatMessageMapper.insert(userMsg);

        // naive retrieval
        QueryWrapper<KnowledgeArticle> qw = new QueryWrapper<>();
        qw.like("title", req.getQuery()).or().like("content", req.getQuery()).last("LIMIT 1");
        KnowledgeArticle hit = knowledgeArticleMapper.selectOne(qw);

        String answer;
        List<String> sources = Collections.emptyList();
        if (hit != null) {
            answer = "根据知识库：\n" + (hit.getSummary() != null ? hit.getSummary() : hit.getContent());
            sources = Collections.singletonList(hit.getSourceUrl() != null ? hit.getSourceUrl() : "内置知识");
        } else {
            if (req.getQuery() != null && req.getQuery().contains("鳞状细胞癌")) {
                answer = "参考：鳞状细胞癌的组织学特征包括角化珠、细胞间桥、异型性明显。建议结合免疫组化和临床信息。";
                sources = Collections.singletonList("WHO肿瘤分类");
            } else {
                answer = "病理诊断需要更详细信息或图片，建议上传病理切片以获得更精准分析。";
            }
        }

        // store AI message
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setSessionId(sessionId);
        aiMsg.setSender("ai");
        aiMsg.setContent(answer);
        aiMsg.setSources(String.join(",", sources));
        aiMsg.setCreatedAt(LocalDateTime.now());
        chatMessageMapper.insert(aiMsg);

        ChatResponse resp = new ChatResponse();
        resp.setAnswer(answer);
        resp.setSources(sources);
        resp.setMessageId(aiMsg.getId());
        return resp;
    }
}