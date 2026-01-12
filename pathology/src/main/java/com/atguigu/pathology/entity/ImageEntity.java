package com.atguigu.pathology.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("image_entity")
public class ImageEntity {
    private Long id;
    private Long uploaderId;
    private String filePath;
    private String fileName;
    private String metadata;
    private String status;
    private LocalDateTime createdAt;
}
