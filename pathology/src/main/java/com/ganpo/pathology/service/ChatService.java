package com.ganpo.pathology.service;

import com.ganpo.pathology.dto.ChatRequest;
import com.ganpo.pathology.dto.ChatResponse;

public interface ChatService {
    ChatResponse chat(ChatRequest req);
}
