package com.atguigu.pathology.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private Long userId;
    private String role; // doctor | patient
    private String query;
    private Long sessionId; // optional
}
