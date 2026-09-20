package com.atguigu.consult.retrieval;

import dev.langchain4j.data.segment.TextSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BM25关键词检索器
 * 基于内存倒排索引实现BM25算法，用于精确关键词匹配
 *
 * BM25算法核心：
 * score(D, Q) = Σ IDF(qi) * (f(qi, D) * (k1 + 1)) / (f(qi, D) + k1 * (1 - b + b * |D| / avgdl))
 *
 * 其中：
 * - f(qi, D): 词qi在文档D中的词频
 * - |D|: 文档D的长度
 * - avgdl: 所有文档的平均长度
 * - k1: 词频饱和参数（默认1.2）
 * - b: 文档长度归一化参数（默认0.75）
 * - IDF(qi): 逆文档频率 = ln((N - n(qi) + 0.5) / (n(qi) + 0.5) + 1)
 */
@Component
public class BM25Retriever {

    private static final Logger log = LoggerFactory.getLogger(BM25Retriever.class);

    private static final double K1 = 1.2;   // 词频饱和参数
    private static final double B = 0.75;   // 文档长度归一化参数

    private final ChineseTokenizer tokenizer = new ChineseTokenizer();

    // 索引数据结构
    private final List<TextSegment> documents = new ArrayList<>();      // 原始文档
    private final List<List<String>> docTokens = new ArrayList<>();     // 每篇文档的分词结果
    private final List<Integer> docLengths = new ArrayList<>();         // 每篇文档的长度
    private final Map<String, Integer> docFreq = new HashMap<>();       // 词 -> 包含该词的文档数
    private double avgDocLength = 0;                                     // 平均文档长度

    /**
     * 构建BM25倒排索引
     *
     * @param segments 待索引的文本段落列表
     */
    public void buildIndex(List<TextSegment> segments) {
        documents.clear();
        docTokens.clear();
        docLengths.clear();
        docFreq.clear();

        int totalLength = 0;

        for (TextSegment segment : segments) {
            String text = segment.text();
            List<String> tokens = tokenizer.tokenize(text);

            documents.add(segment);
            docTokens.add(tokens);
            docLengths.add(tokens.size());
            totalLength += tokens.size();

            // 统计词频（每篇文档中每个词只计一次）
            new HashMap<String, Integer>() {{
                for (String token : tokens) {
                    put(token, 1);
                }
            }}.keySet().forEach(word -> 
                docFreq.merge(word, 1, Integer::sum)
            );
        }

        avgDocLength = documents.isEmpty() ? 0 : (double) totalLength / documents.size();
        log.info("BM25索引构建完成: 文档数={}, 平均文档长度={}", documents.size(), String.format("%.1f", avgDocLength));
    }

    /**
     * 检索与查询最相关的文档
     *
     * @param queryText 查询文本
     * @param maxResults 最大返回结果数
     * @param minScore 最小分数阈值
     * @return 检索结果列表（按BM25得分降序排列）
     */
    public List<BM25Result> retrieve(String queryText, int maxResults, double minScore) {
        if (documents.isEmpty()) {
            log.warn("BM25索引为空，请先调用buildIndex构建索引");
            return new ArrayList<>();
        }

        List<String> queryTokens = tokenizer.tokenize(queryText);
        if (queryTokens.isEmpty()) {
            return new ArrayList<>();
        }

        // 计算每篇文档的BM25得分
        List<BM25Result> results = new ArrayList<>();
        int N = documents.size();

        for (int i = 0; i < N; i++) {
            double score = calculateBM25Score(queryTokens, i, N);
            if (score >= minScore) {
                results.add(new BM25Result(documents.get(i), score, i));
            }
        }

        // 按得分降序排列，取topK
        results.sort((a, b) -> Double.compare(b.score, a.score));
        
        if (results.size() > maxResults) {
            results = results.subList(0, maxResults);
        }

        log.debug("BM25检索: query='{}', 命中{}条结果", queryText, results.size());
        return results;
    }

    /**
     * 计算单篇文档的BM25得分
     */
    private double calculateBM25Score(List<String> queryTokens, int docIndex, int totalDocs) {
        double score = 0;
        List<String> currentDocTokens = docTokens.get(docIndex);
        int docLen = docLengths.get(docIndex);

        // 统计查询词在文档中的词频
        Map<String, Integer> termFreqInDoc = new HashMap<>();
        for (String token : currentDocTokens) {
            termFreqInDoc.merge(token, 1, Integer::sum);
        }

        for (String queryToken : queryTokens) {
            Integer tf = termFreqInDoc.get(queryToken);
            if (tf == null || tf == 0) {
                continue; // 该词不在文档中，跳过
            }

            Integer df = docFreq.get(queryToken);
            if (df == null || df == 0) {
                continue;
            }

            // IDF计算
            double idf = Math.log((double) (totalDocs - df + 0.5) / (df + 0.5) + 1);

            // BM25得分公式
            double tfComponent = (tf * (K1 + 1)) / (tf + K1 * (1 - B + B * docLen / avgDocLength));

            score += idf * tfComponent;
        }

        return score;
    }

    public int getDocumentCount() {
        return documents.size();
    }

    /**
     * BM25检索结果
     */
    public static class BM25Result {
        public final TextSegment segment;
        public final double score;
        public final int docIndex;

        public BM25Result(TextSegment segment, double score, int docIndex) {
            this.segment = segment;
            this.score = score;
            this.docIndex = docIndex;
        }
    }
}
