package com.atguigu.pathology.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("knowledge_article")
public class KnowledgeArticle {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String category;
    private String sourceUrl;
    private String evidenceLevel;
    private Long createdBy;
    private LocalDateTime createdAt;
}
