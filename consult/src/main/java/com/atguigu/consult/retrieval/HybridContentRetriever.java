package com.atguigu.consult.retrieval;

import com.atguigu.consult.skills.SkillManager;
import com.atguigu.consult.trace.TraceService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 混合检索器（Pinecone + FAISS双引擎 + BM25关键词检索）
 *
 * 检索流程：
 * 1. 意图感知路由：通过 QueryIntentClassifier 识别用户查询意图
 * 2. 多路并行检索：
 *    a. Pinecone 云端向量检索（语义召回）
 *    b. FAISS/InMemory 本地向量检索（本地快速语义召回）
 *    c. BM25 关键词检索（精确匹配，弥补向量检索对专业术语缩写召回不足）
 * 3. RRF融合排序：基于 Reciprocal Rank Fusion 算法融合多路检索结果
 * 4. 根据意图动态调整各引擎权重：
 *    - 医院查询：关键词权重高（0.7），向量权重低（0.3）
 *    - 疾病科普：向量权重高（0.7），关键词权重低（0.3）
 *    - 政策咨询：均衡权重（0.5:0.5）
 *    - 症状自查：向量权重最高（0.8），关键词权重低（0.2）
 */
@Component
public class HybridContentRetriever implements ContentRetriever {

    private static final Logger log = LoggerFactory.getLogger(HybridContentRetriever.class);

    // 检索参数
    private static final int VECTOR_MAX_RESULTS = 3;   // 每路向量检索最大返回数
    private static final double VECTOR_MIN_SCORE = 0.6; // 向量检索最低相似度
    private static final int BM25_MAX_RESULTS = 5;      // BM25检索最大返回数
    private static final double BM25_MIN_SCORE = 0.01;  // BM25最低分数
    private static final int FINAL_MAX_RESULTS = 3;     // RRF融合后最终返回数

    @Autowired
    private QueryIntentClassifier intentClassifier;

    @Autowired
    private SkillManager skillManager;

    @Autowired
    private TraceService traceService;

    @Autowired
    private BM25Retriever bm25Retriever;

    @Autowired
    @Qualifier("localEmbeddingStore")
    private InMemoryEmbeddingStore<TextSegment> localEmbeddingStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    @Qualifier("embeddingStore")
    private EmbeddingStore<TextSegment> pineconeEmbeddingStore;

    private final RRFMerger rrfMerger = new RRFMerger();

    // 预构建的检索器（避免每次查询都创建）
    private EmbeddingStoreContentRetriever pineconeRetriever;
    private EmbeddingStoreContentRetriever localRetriever;

    @PostConstruct
    public void init() {
        // Pinecone 云端向量检索器
        try {
            pineconeRetriever = EmbeddingStoreContentRetriever.builder()
                    .embeddingStore(pineconeEmbeddingStore)
                    .embeddingModel(embeddingModel)
                    .maxResults(VECTOR_MAX_RESULTS)
                    .minScore(VECTOR_MIN_SCORE)
                    .build();
            log.info("Pinecone向量检索器初始化成功");
        } catch (Exception e) {
            log.warn("Pinecone向量检索器初始化失败: {}", e.getMessage());
        }

        // FAISS/InMemory 本地向量检索器
        try {
            localRetriever = EmbeddingStoreContentRetriever.builder()
                    .embeddingStore(localEmbeddingStore)
                    .embeddingModel(embeddingModel)
                    .maxResults(VECTOR_MAX_RESULTS)
                    .minScore(VECTOR_MIN_SCORE)
                    .build();
            log.info("本地向量检索器(FAISS)初始化成功");
        } catch (Exception e) {
            log.warn("本地向量检索器初始化失败: {}", e.getMessage());
        }
    }

    @Override
    public List<Content> retrieve(Query query) {
        String queryText = query.text();

        // 1. 意图感知路由
        QueryIntent intent = intentClassifier.classify(queryText);
        log.info("查询意图: {} (向量权重={}, 关键词权重={}) | 查询: '{}'",
                intent.getDescription(), intent.getVectorWeight(), intent.getKeywordWeight(), queryText);

        // 2. 多路并行检索
        List<TextSegment> vectorResults = new ArrayList<>();
        List<TextSegment> keywordResults = new ArrayList<>();

        // 2a. Pinecone 云端向量检索
        long pineconeStart = System.currentTimeMillis();
        int pineconeCount = 0;
        try {
            if (pineconeRetriever != null) {
                List<Content> pineconeContents = pineconeRetriever.retrieve(query);
                pineconeCount = pineconeContents.size();
                vectorResults.addAll(pineconeContents.stream()
                        .map(Content::textSegment)
                        .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            log.warn("Pinecone检索异常: {}", e.getMessage());
        }
        long pineconeTime = System.currentTimeMillis() - pineconeStart;

        // 2b. FAISS/InMemory 本地向量检索
        long localStart = System.currentTimeMillis();
        int localCount = 0;
        try {
            if (localRetriever != null) {
                List<Content> localContents = localRetriever.retrieve(query);
                localCount = localContents.size();
                vectorResults.addAll(localContents.stream()
                        .map(Content::textSegment)
                        .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            log.warn("本地向量检索异常: {}", e.getMessage());
        }
        long localTime = System.currentTimeMillis() - localStart;

        // 2c. BM25 关键词检索
        long bm25Start = System.currentTimeMillis();
        try {
            List<BM25Retriever.BM25Result> bm25Results = bm25Retriever.retrieve(
                    queryText, BM25_MAX_RESULTS, BM25_MIN_SCORE);
            keywordResults = bm25Results.stream()
                    .map(r -> r.segment)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("BM25检索异常: {}", e.getMessage());
        }
        long bm25Time = System.currentTimeMillis() - bm25Start;

        log.info("多路检索完成: Pinecone={}条({}ms), 本地向量={}条({}ms), BM25={}条({}ms)",
                pineconeCount, pineconeTime, localCount, localTime,
                keywordResults.size(), bm25Time);

        // 3. RRF融合排序（根据意图权重）
        List<TextSegment> merged = rrfMerger.merge(
                vectorResults, intent.getVectorWeight(),
                keywordResults, intent.getKeywordWeight(),
                FINAL_MAX_RESULTS
        );

        log.info("RRF融合结果: {}条", merged.size());

        // Trace：记录检索统计（同线程内生效，跨线程时自动跳过）
        traceService.recordEvent("retrieval", java.util.Map.of(
                "intent", intent.name(),
                "pineconeCount", pineconeCount,
                "localCount", localCount,
                "bm25Count", keywordResults.size(),
                "mergedCount", merged.size()));

        // 4. Skills 注入：将意图匹配的技能文档（方法论/流程性知识）置顶注入上下文，
        //    与检索到的事实性知识互补（Skill 负责"怎么做"，检索内容负责"是什么"）
        List<Content> results = new ArrayList<>();
        String skill = skillManager.getSkillForIntent(intent);
        if (skill != null && !skill.isBlank()) {
            results.add(Content.from(TextSegment.from(skill)));
            log.info("已注入技能: {}", intent.getDescription());
        }

        // 5. 追加融合后的检索结果
        results.addAll(merged.stream()
                .map(Content::from)
                .collect(Collectors.toList()));
        return results;
    }
}
