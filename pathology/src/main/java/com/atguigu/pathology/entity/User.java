package com.atguigu.pathology.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    private Long id;
    private String username;
    private String password;
    private String role;
    private Boolean verified;
    private String institution;
    private String doctorName;
    private LocalDateTime createdAt;
}
