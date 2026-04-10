package com.atguigu.medicalresource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("departments")
public class Department {
    private String id;
    private String name;
    private String parentId;
    private String alias; // JSON string
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}