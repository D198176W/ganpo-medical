package com.atguigu.pathology.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    private String answer;
    private List<String> sources;
    private Long messageId;
}
