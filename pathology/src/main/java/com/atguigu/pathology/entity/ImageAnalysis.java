package com.atguigu.pathology.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("image_analysis")
public class ImageAnalysis {
    private Long id;
    private Long imageId;
    private String modelVersion;
    private String result;
    private String confidence;
    private Long reviewerId;
    private LocalDateTime reviewedAt;
}