package com.atguigu.medicalresource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("hospitals")
public class Hospital {
    private Long id;
    private String name;
    private String level;
    private Double rating;
    private String address;
    private Double lat;
    private Double lng;
    private String phone;
    private String coreDepartments; // JSON array
    private Boolean hasOpenSlots;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
