package com.atguigu.pathology.service;

import com.atguigu.pathology.dto.ChatRequest;
import com.atguigu.pathology.entity.ChatMessage;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {
    Flux<String> chatStream(ChatRequest req);
    List<ChatMessage> getHistory(String sessionId);
    void clearHistory(String sessionId);
}
