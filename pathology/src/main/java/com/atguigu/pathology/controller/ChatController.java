package com.atguigu.pathology.controller;

import com.atguigu.pathology.dto.ChatRequest;
import com.atguigu.pathology.entity.ChatMessage;
import com.atguigu.pathology.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatRequest req) {
        return chatService.chatStream(req);
    }

    @GetMapping("/history/{sessionId}")
    public List<ChatMessage> getHistory(@PathVariable String sessionId) {
        try {
            return chatService.getHistory(sessionId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @DeleteMapping("/history/{sessionId}")
    public String clearHistory(@PathVariable String sessionId) {
        try {
            chatService.clearHistory(sessionId);
            return "清除成功";
        } catch (Exception e) {
            return "清除失败: " + e.getMessage();
        }
    }
}
