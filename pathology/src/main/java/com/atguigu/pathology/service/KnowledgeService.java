package com.atguigu.pathology.service;

import com.atguigu.pathology.entity.KnowledgeArticle;

import java.util.List;

public interface KnowledgeService {
    KnowledgeArticle create(KnowledgeArticle article);
    KnowledgeArticle getById(Long id);
    List<KnowledgeArticle> listByCategory(String category);
}
