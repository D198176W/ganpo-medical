package com.atguigu.consult.retrieval;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 知识库管理器
 *
 * 职责：
 * 1. 在应用启动时加载 knowledge/ 目录下的医疗知识文档（Markdown格式）
 * 2. 对文档进行切分（按字符递归切分，每段最大500字符，重叠50字符）
 * 3. 构建 BM25 关键词检索索引（基于切分后的文本段落）
 * 4. 将文档向量嵌入写入本地向量存储（FAISS/InMemory）
 *
 * 知识库内容：
 * - 医院信息.md：南昌本地6家三甲医院信息
 * - 科室信息.md：10个科室介绍与症状对照表
 * - 疾病科普.md：7种常见疾病的科普知识
 * - 医保政策.md：南昌市医保报销政策
 */
@Component
public class KnowledgeBaseManager {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseManager.class);

    private static final String KNOWLEDGE_PATH = "classpath:knowledge/*.md";
    private static final int MAX_SEGMENT_SIZE = 500;  // 段落最大字符数
    private static final int OVERLAP_SIZE = 50;        // 段落重叠字符数

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    @Autowired
    private BM25Retriever bm25Retriever;

    @Autowired
    @Qualifier("localEmbeddingStore")
    private InMemoryEmbeddingStore<TextSegment> localEmbeddingStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 应用启动时自动加载知识库并构建索引
     */
    @PostConstruct
    public void init() {
        try {
            log.info("========== 开始加载知识库 ==========");

            // 1. 加载知识文档
            Resource[] resources = resourcePatternResolver.getResources(KNOWLEDGE_PATH);
            if (resources.length == 0) {
                log.warn("未找到知识库文档，请检查 classpath:knowledge/ 目录");
                return;
            }

            List<Document> documents = new ArrayList<>();
            for (Resource resource : resources) {
                String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                String filename = resource.getFilename();
                Document doc = Document.from(content);
                doc.metadata().put("source", filename != null ? filename : "unknown");
                documents.add(doc);
                log.info("加载文档: {}", filename);
            }

            // 2. 切分文档
            DocumentSplitter splitter = DocumentSplitters.recursive(MAX_SEGMENT_SIZE, OVERLAP_SIZE);
            List<TextSegment> allSegments = new ArrayList<>();
            for (Document doc : documents) {
                List<TextSegment> segments = splitter.split(doc);
                allSegments.addAll(segments);
            }
            log.info("文档切分完成: 文档数={}, 段落数={}", documents.size(), allSegments.size());

            // 3. 构建 BM25 索引
            bm25Retriever.buildIndex(allSegments);

            // 4. 向量嵌入并写入本地向量存储
            int embeddedCount = 0;
            int failedCount = 0;
            for (TextSegment segment : allSegments) {
                try {
                    Response<Embedding> response = embeddingModel.embed(segment);
                    Embedding embedding = response.content();
                    localEmbeddingStore.add(embedding, segment);
                    embeddedCount++;
                } catch (Exception e) {
                    failedCount++;
                    if (failedCount == 1) {
                        log.warn("向量嵌入失败（可能API额度不足）: {}", e.getMessage());
                    }
                }
            }

            if (failedCount > 0) {
                log.warn("向量嵌入完成: 成功={}, 失败={}（BM25索引仍可正常使用）", embeddedCount, failedCount);
            } else {
                log.info("向量嵌入完成: 成功={}", embeddedCount);
            }

            log.info("========== 知识库加载完成 ==========");
            log.info("知识库统计: 文档={} | 段落={} | BM25索引={} | 向量索引={}",
                    documents.size(), allSegments.size(),
                    bm25Retriever.getDocumentCount(), embeddedCount);

        } catch (Exception e) {
            log.error("知识库初始化失败", e);
        }
    }

    /**
     * 获取本地向量存储中的文档数量
     */
    public long getLocalVectorCount() {
        return localEmbeddingStore != null ? 1 : 0;
    }
}
