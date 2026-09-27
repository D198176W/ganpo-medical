package com.ganpo.pathology.service;

import com.ganpo.pathology.entity.KnowledgeArticle;

import java.util.List;

public interface KnowledgeService {
    KnowledgeArticle create(KnowledgeArticle article);
    KnowledgeArticle getById(Long id);
    List<KnowledgeArticle> listByCategory(String category);
}
