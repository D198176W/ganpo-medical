package com.atguigu.pathology.controller;

import com.atguigu.pathology.entity.ImageAnalysis;
import com.atguigu.pathology.entity.ImageEntity;
import com.atguigu.pathology.service.ImageService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        Object claimsObj = request.getAttribute("claims");
        Long uid = null;
        if (claimsObj != null) {
            io.jsonwebtoken.Claims claims = (io.jsonwebtoken.Claims)claimsObj;
            uid = claims.get("uid", Long.class);
        }
        if (uid == null) uid = 0L;

        ImageEntity entity = imageService.store(file, uid);
        imageService.analyzeAsync(entity.getId());
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/{imageId}/analysis")
    public ResponseEntity<ImageAnalysis> getAnalysis(@PathVariable Long imageId) {
        return ResponseEntity.ok(imageService.getAnalysisByImageId(imageId));
    }
}