package com.atguigu.pathology.controller;

import com.atguigu.pathology.entity.KnowledgeArticle;
import com.atguigu.pathology.service.KnowledgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {
    private final KnowledgeService knowledgeService;
    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @PostMapping
    public ResponseEntity<KnowledgeArticle> create(@RequestBody KnowledgeArticle article) {
        KnowledgeArticle saved = knowledgeService.create(article);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeArticle> get(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.getById(id));
    }

    @GetMapping("/category/{cat}")
    public ResponseEntity<List<KnowledgeArticle>> listByCategory(@PathVariable("cat") String cat) {
        return ResponseEntity.ok(knowledgeService.listByCategory(cat));
    }
}