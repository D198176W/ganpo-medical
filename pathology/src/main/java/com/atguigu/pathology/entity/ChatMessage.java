package com.atguigu.pathology.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_message")
public class ChatMessage {
    private Long id;
    private Long sessionId;
    private String sender; // user | ai
    private String content;
    private String sources;
    private LocalDateTime createdAt;
}
