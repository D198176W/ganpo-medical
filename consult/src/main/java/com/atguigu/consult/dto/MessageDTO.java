package com.atguigu.consult.dto;

import lombok.Data;

@Data  // 使用lombok简化getter/setter
public class MessageDTO {
    private String id;      // 消息唯一标识
    private String sender;  // 发送者（"user"或"bot"）
    private String text;    // 消息内容
    // 可选：添加时间戳字段
    // private long timestamp;
}