package com.atguigu.pathology.dto;

import lombok.Data;

@Data
public class ChatRequest {

    private String userId; // 改为 String 类型，以兼容前端的 userId 格式
    private String role; // doctor | patient
    private String query;
    private String sessionId; // optional

}
