package com.atguigu.medicalresource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
    private LocalDateTime createdAt;
}