package com.atguigu.pathology.service.impl;

import com.atguigu.pathology.entity.ImageAnalysis;
import com.atguigu.pathology.entity.ImageEntity;
import com.atguigu.pathology.mapper.ImageAnalysisMapper;
import com.atguigu.pathology.mapper.ImageMapper;
import com.atguigu.pathology.service.ImageService;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${app.storage.upload-dir}")
    private String uploadDir;

    private final ImageMapper imageMapper;
    private final ImageAnalysisMapper analysisMapper;

    public ImageServiceImpl(ImageMapper imageMapper, ImageAnalysisMapper analysisMapper) {
        this.imageMapper = imageMapper;
        this.analysisMapper = analysisMapper;
    }

    @Override
    public ImageEntity store(MultipartFile file, Long uploaderId) throws Exception {
       File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File dest = new File(dir, filename);
        file.transferTo(dest);

        ImageEntity entity = new ImageEntity();
        entity.setUploaderId(uploaderId);
        entity.setFilePath(dest.getAbsolutePath());
        entity.setFileName(file.getOriginalFilename());
        entity.setMetadata("{}");
        entity.setStatus("uploaded");
        entity.setCreatedAt(LocalDateTime.now());
        imageMapper.insert(entity);
        return entity;
    }

    @Async("taskExecutor")
    @Override
    public ImageAnalysis analyzeAsync(Long imageId) {
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        ImageAnalysis an = new ImageAnalysis();
        an.setImageId(imageId);
        an.setModelVersion("v0.1-demo");
        an.setResult("初步结论：可能为鳞状细胞癌（中度分化）；细胞特征：角化珠、核异型；建议：免疫组化进一步确认。");
        an.setConfidence("80%");
        an.setReviewedAt(LocalDateTime.now());
        analysisMapper.insert(an);

        ImageEntity img = imageMapper.selectById(imageId);
        if (img != null) {
            img.setStatus("analyzed");
            imageMapper.updateById(img);
        }

        return an;
    }

    @Override
    public ImageAnalysis getAnalysisByImageId(Long imageId) {
        return analysisMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ImageAnalysis>().eq("image_id", imageId));
    }
}
