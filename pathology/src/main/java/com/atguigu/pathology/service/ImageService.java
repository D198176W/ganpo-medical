package com.atguigu.pathology.service;

import com.atguigu.pathology.entity.ImageAnalysis;
import com.atguigu.pathology.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageEntity store(MultipartFile file, Long uploaderId) throws Exception;
    ImageAnalysis analyzeAsync(Long imageId);
    ImageAnalysis getAnalysisByImageId(Long imageId);
}