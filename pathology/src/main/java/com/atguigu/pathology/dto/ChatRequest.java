package com.atguigu.pathology.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String userId;
    private String role; // doctor | patient
    private String query;
    private String sessionId; // optional
}
