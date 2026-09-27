package com.ganpo.pathology.service;

import com.ganpo.pathology.entity.ImageAnalysis;
import com.ganpo.pathology.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageEntity store(MultipartFile file, Long uploaderId) throws Exception;
    ImageAnalysis analyzeAsync(Long imageId);
    ImageAnalysis getAnalysisByImageId(Long imageId);
}