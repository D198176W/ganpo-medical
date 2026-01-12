package com.atguigu.pathology.service;

import com.atguigu.pathology.dto.ChatRequest;
import com.atguigu.pathology.dto.ChatResponse;

public interface ChatService {
    ChatResponse chat(ChatRequest req);
}
