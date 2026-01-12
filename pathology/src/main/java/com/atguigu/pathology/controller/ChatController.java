package com.atguigu.pathology.controller;

import com.atguigu.pathology.dto.ChatRequest;
import com.atguigu.pathology.dto.ChatResponse;
import com.atguigu.pathology.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
//    @Autowired
    private final ChatService chatService;
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest req) {
        ChatResponse resp = chatService.chat(req);
        return ResponseEntity.ok(resp);
    }
}