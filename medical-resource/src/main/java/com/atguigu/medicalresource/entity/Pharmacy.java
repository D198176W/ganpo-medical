package com.atguigu.medicalresource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pharmacies")
public class Pharmacy {
    private Long id;
    private String name;
    private String address;
    private Double lat;
    private Double lng;
    private String phone;
    private Boolean isDesignated;
    private String openingHours;
    private LocalDateTime createdAt;
}