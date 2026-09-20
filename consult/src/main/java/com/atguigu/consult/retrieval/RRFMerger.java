package com.atguigu.consult.retrieval;

import dev.langchain4j.data.segment.TextSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reciprocal Rank Fusion (RRF) 融合排序算法
 *
 * RRF核心思想：不依赖各检索引擎的原始分数，仅利用排名位置进行融合
 * 避免了不同检索引擎分数尺度不一致的问题（向量相似度分数 vs BM25分数）
 *
 * RRF公式：
 *   RRF_score(d) = Σ (1 / (k + rank_i(d)))
 *
 * 其中：
 *   - rank_i(d): 文档d在第i个检索引擎结果中的排名（从1开始）
 *   - k: 平滑常数（默认60），k越大对排名差异的惩罚越温和
 *   - 对每个检索引擎的结果列表分别计算RRF分数后求和
 *
 * 加权RRF变体：
 *   RRF_score(d) = Σ weight_i * (1 / (k + rank_i(d)))
 * 根据意图路由分配的权重，对不同引擎的结果进行加权融合
 */
public class RRFMerger {

    private static final Logger log = LoggerFactory.getLogger(RRFMerger.class);

    private static final int DEFAULT_K = 60; // RRF平滑常数

    /**
     * 对多路检索结果进行RRF融合排序
     *
     * @param vectorResults  向量检索结果列表（已按相似度降序排列）
     * @param vectorWeight   向量检索权重
     * @param keywordResults 关键词检索结果列表（已按BM25得分降序排列）
     * @param keywordWeight  关键词检索权重
     * @param maxResults     最终返回的最大结果数
     * @return 融合排序后的文本段落列表
     */
    public List<TextSegment> merge(
            List<TextSegment> vectorResults, double vectorWeight,
            List<TextSegment> keywordResults, double keywordWeight,
            int maxResults) {

        // 文档文本 -> RRF得分
        Map<String, RRFEntry> scoreMap = new HashMap<>();

        // 处理向量检索结果
        for (int i = 0; i < vectorResults.size(); i++) {
            TextSegment segment = vectorResults.get(i);
            int rank = i + 1; // 排名从1开始
            double rrfScore = vectorWeight * (1.0 / (DEFAULT_K + rank));
            scoreMap.computeIfAbsent(segment.text(), k -> new RRFEntry(segment))
                    .addScore(rrfScore);
        }

        // 处理关键词检索结果
        for (int i = 0; i < keywordResults.size(); i++) {
            TextSegment segment = keywordResults.get(i);
            int rank = i + 1;
            double rrfScore = keywordWeight * (1.0 / (DEFAULT_K + rank));
            scoreMap.computeIfAbsent(segment.text(), k -> new RRFEntry(segment))
                    .addScore(rrfScore);
        }

        // 按RRF总分降序排列
        List<RRFEntry> entries = new ArrayList<>(scoreMap.values());
        entries.sort((a, b) -> Double.compare(b.totalScore, a.totalScore));

        // 取topK
        List<TextSegment> result = new ArrayList<>();
        for (int i = 0; i < Math.min(maxResults, entries.size()); i++) {
            result.add(entries.get(i).segment);
        }

        log.debug("RRF融合: 向量结果{}条(权重{}), 关键词结果{}条(权重{}), 融合后{}条",
                vectorResults.size(), vectorWeight,
                keywordResults.size(), keywordWeight,
                result.size());

        return result;
    }

    /**
     * RRF条目，记录文档及其累计RRF分数
     */
    private static class RRFEntry {
        final TextSegment segment;
        double totalScore;
        int hitCount; // 被多少个引擎命中

        RRFEntry(TextSegment segment) {
            this.segment = segment;
            this.totalScore = 0;
            this.hitCount = 0;
        }

        void addScore(double score) {
            this.totalScore += score;
            this.hitCount++;
        }
    }
}
