package com.atguigu.consult.evals;

import com.atguigu.consult.retrieval.HybridContentRetriever;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RAG 检索准确率评测
 *
 * 加载 evals/retrieval-eval.json 评测集，对每条用例执行混合检索，
 * 判定标准：任一返回片段包含期望关键词。输出命中率报告。
 *
 * 需要真实向量服务与 Embedding API，通过环境变量门控（CI 中配置后自动启用）：
 *   DASHSCOPE_API_KEY、PINECONE_API_KEY
 */
@SpringBootTest
@EnabledIfEnvironmentVariable(named = "DASHSCOPE_API_KEY", matches = ".+")
@EnabledIfEnvironmentVariable(named = "PINECONE_API_KEY", matches = ".+")
class RagRetrievalEvalTest {

    private static final double ACCURACY_THRESHOLD = 0.70;

    @Autowired
    private HybridContentRetriever retriever;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void retrievalAccuracy() throws Exception {
        List<Map<String, String>> cases = objectMapper.readValue(
                new ClassPathResource("evals/retrieval-eval.json").getInputStream(),
                new TypeReference<>() {});

        int hit = 0;
        StringBuilder report = new StringBuilder("\n========== RAG 检索评测报告 ==========\n");
        for (Map<String, String> c : cases) {
            String query = c.get("query");
            String expect = c.get("expect");
            List<Content> results = retriever.retrieve(Query.from(query));
            boolean matched = results.stream()
                    .anyMatch(content -> content.textSegment().text().contains(expect));
            if (matched) {
                hit++;
            }
            report.append(String.format("[%s] %s -> 期望含「%s」%n", matched ? "命中" : "未中", query, expect));
        }

        double accuracy = hit / (double) cases.size();
        report.append(String.format("检索命中率: %d/%d = %.1f%%（阈值 %.0f%%）%n",
                hit, cases.size(), accuracy * 100, ACCURACY_THRESHOLD * 100));
        System.out.println(report);

        assertTrue(accuracy >= ACCURACY_THRESHOLD,
                "RAG 检索准确率应 >= " + ACCURACY_THRESHOLD + "，实际: " + accuracy);
    }
}
