package com.atguigu.pathology.service.impl;

import com.atguigu.pathology.entity.KnowledgeArticle;
import com.atguigu.pathology.mapper.KnowledgeArticleMapper;
import com.atguigu.pathology.service.KnowledgeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    private final KnowledgeArticleMapper mapper;

    public KnowledgeServiceImpl(KnowledgeArticleMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public KnowledgeArticle create(KnowledgeArticle article) {
        mapper.insert(article);
        return article;
    }

    @Override
    public KnowledgeArticle getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public List<KnowledgeArticle> listByCategory(String category) {
        QueryWrapper<KnowledgeArticle> qw = new QueryWrapper<>();
        qw.eq("category", category);
        return mapper.selectList(qw);
    }
}
