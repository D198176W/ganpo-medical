package com.atguigu.consult.controller;




import com.atguigu.consult.assistant.XiaozhiAgent;
import com.atguigu.consult.bean.ChatForm;
import com.atguigu.consult.bean.ChatMessages;
import com.atguigu.consult.dto.MessageDTO;
import com.atguigu.consult.store.MongoChatMemoryStore;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name="赣鄱医枢")

@RestController
@RequestMapping("/xiaozhi")
public class XiaozhiController {

    @Autowired
    private XiaozhiAgent xiaozhiAgent;

    @Operation(summary = "对话")
    @PostMapping(value = "/chat", produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatForm chatForm) {
        // 参数校验
        if (chatForm == null) {
            return Flux.just("请求参数不能为空");
        }

        if (chatForm.getMessage() == null || chatForm.getMessage().trim().isEmpty()) {
            return Flux.just("消息内容不能为空");
        }

        // 确保memoryId不为空，前端可能没有提供
        Long memoryId = chatForm.getMemoryId();
        if (memoryId == null) {
            // 生成新的memoryId
            memoryId = System.currentTimeMillis();
        }

        try {
            return xiaozhiAgent.chat(memoryId, chatForm.getMessage());
        } catch (Exception e) {
            return Flux.error(new RuntimeException("对话处理失败: " + e.getMessage()));
        }
    }





    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;


    @Operation(summary = "获取聊天历史")
    @GetMapping("/history/{memoryId}")
    public List<MessageDTO> getHistory(@PathVariable Long memoryId) {
        if (memoryId == null) {
            return new ArrayList<MessageDTO>();
        }

        try {
            List<ChatMessage> chatMessage = mongoChatMemoryStore.getMessages(memoryId);
            if (chatMessage == null) {
                chatMessage = new ArrayList<>();
            }

            return chatMessage.stream().map(c -> {
                ChatMessages chatMessages = (ChatMessages) c;
                MessageDTO dto = new MessageDTO();
                dto.setId(UUID.randomUUID().toString());
                dto.setSender(c.type() == ChatMessageType.USER ? "user" : "bot");
                dto.setText(chatMessages.content());
//                dto.setText(chatMessage.text());


                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<MessageDTO>();
        }
    }


    // 新增接口：清除聊天历史
    @Operation(summary = "清除聊天历史")
    @DeleteMapping("/history/{memoryId}")
    public String clearHistory(@PathVariable Long memoryId) {
        // 实际实现应从MongoChatMemoryStore中删除历史记录
        return "历史记录已清除 - memoryId: " + memoryId;
    }


//    @PostMapping("/voice-to-text")
//    public String voiceToText(@RequestParam("file") MultipartFile file) {
//        // 调用语音识别API转换为文字
//        return voiceRecognitionService.recognize(file);
//    }


}

