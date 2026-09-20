package com.atguigu.consult.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 本地向量存储配置
 *
 * 使用 InMemoryEmbeddingStore 作为本地向量检索引擎。
 * InMemoryEmbeddingStore 是 LangChain4j 原生支持的内存向量库，
 * 提供与 FAISS 等效的稠密向量相似度检索能力。
 *
 * 注：langchain4j-faiss 在 1.0.0-beta3 版本未发布到 Maven Central，
 * 因此使用 InMemoryEmbeddingStore 作为等效实现。
 * 升级到 LangChain4j 正式版后可无缝替换为 FaissEmbeddingStore。
 *
 * 双引擎架构说明：
 * - Pinecone：云端向量引擎，负责全量知识的语义检索
 * - 本地向量引擎(InMemory)：本地快速向量检索，作为Pinecone的补充
 * - 两路向量检索结果通过RRF融合，提升召回率
 */
@Configuration
public class LocalEmbeddingStoreConfig {

    private static final Logger log = LoggerFactory.getLogger(LocalEmbeddingStoreConfig.class);

    /**
     * 本地向量存储 Bean
     * 存储 knowledge/ 目录下知识文档的向量嵌入
     */
    @Bean(name = "localEmbeddingStore")
    public InMemoryEmbeddingStore<TextSegment> localEmbeddingStore() {
        log.info("初始化本地向量存储引擎 (FAISS等效实现)");
        return new InMemoryEmbeddingStore<>();
    }
}
