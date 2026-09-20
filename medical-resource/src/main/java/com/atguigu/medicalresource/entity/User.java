package com.atguigu.medicalresource.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {

    private Long id;
    private String username;
    private String password; // 已加密（bcrypt）
    private String name;
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private LocalDateTime createdAt;
}