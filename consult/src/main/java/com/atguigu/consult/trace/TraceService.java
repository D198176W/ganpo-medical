package com.atguigu.consult.trace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 全链路 Trace 服务
 *
 * 每次对话生成一条 Trace，记录：意图、检索统计、Skill 注入、Reflector 自审结果、
 * 时延、估算 token 消耗，落盘 logs/traces/{traceId}.json 支持事后回放与瓶颈定位；
 * 内存保留最近 100 条摘要，供指标接口查询（成功率 / 平均时延 / token 成本趋势）。
 */
@Service
public class TraceService {

    private static final Logger log = LoggerFactory.getLogger(TraceService.class);
    private static final Path TRACE_DIR = Paths.get("logs", "traces");
    private static final int RECENT_CACHE_SIZE = 100;

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final Tokenizer tokenizer = new OpenAiTokenizer("gpt-4");

    /** 当前线程的 Trace（一次对话一个） */
    private final ThreadLocal<Trace> currentTrace = new ThreadLocal<>();

    /** 最近 Trace 摘要（环形缓冲） */
    private final ConcurrentLinkedDeque<TraceSummary> recentTraces = new ConcurrentLinkedDeque<>();

    public Trace beginTrace(Long memoryId, String question) {
        Trace trace = new Trace();
        trace.traceId = UUID.randomUUID().toString().substring(0, 8);
        trace.memoryId = memoryId;
        trace.question = question;
        trace.startTime = Instant.now().toString();
        trace.startMillis = System.currentTimeMillis();
        currentTrace.set(trace);
        return trace;
    }

    /** 在链路任意位置记录事件（意图、检索、自审等），同线程内生效（best-effort） */
    public void recordEvent(String stage, Map<String, Object> data) {
        Trace trace = currentTrace.get();
        if (trace != null) {
            recordEvent(trace, stage, data);
        }
    }

    /** 显式记录事件到指定 Trace（跨线程场景使用） */
    public void recordEvent(Trace trace, String stage, Map<String, Object> data) {
        if (trace != null) {
            synchronized (trace.events) {
                trace.events.add(new TraceEvent(stage, Instant.now().toString(), data));
            }
        }
    }

    /** 结束 Trace：计算时延与 token 估算，落盘 + 写入摘要缓存 */
    public void endTrace(Trace trace, String answer, List<String> violations) {
        currentTrace.remove();
        if (trace == null) {
            return;
        }
        trace.endTime = Instant.now().toString();
        trace.latencyMs = System.currentTimeMillis() - trace.startMillis;
        trace.answerLength = answer == null ? 0 : answer.length();
        trace.reflectionViolations = violations == null ? List.of() : violations;
        trace.success = trace.reflectionViolations.isEmpty();
        try {
            trace.estimatedQuestionTokens = tokenizer.estimateTokenCountInText(trace.question);
            trace.estimatedAnswerTokens = tokenizer.estimateTokenCountInText(answer == null ? "" : answer);
        } catch (Exception e) {
            log.debug("token 估算失败: {}", e.getMessage());
        }

        persist(trace);
        cacheSummary(trace);
    }

    public void endTraceWithError(Trace trace, String errorMessage) {
        currentTrace.remove();
        if (trace == null) {
            return;
        }
        trace.endTime = Instant.now().toString();
        trace.latencyMs = System.currentTimeMillis() - trace.startMillis;
        trace.success = false;
        trace.error = errorMessage;
        persist(trace);
        cacheSummary(trace);
    }

    public List<TraceSummary> getRecentTraces() {
        return new ArrayList<>(recentTraces);
    }

    private void persist(Trace trace) {
        try {
            Files.createDirectories(TRACE_DIR);
            Path file = TRACE_DIR.resolve(trace.traceId + ".json");
            objectMapper.writeValue(file.toFile(), trace);
        } catch (IOException e) {
            log.warn("Trace 落盘失败: {}", e.getMessage());
        }
    }

    private void cacheSummary(Trace trace) {
        recentTraces.addFirst(new TraceSummary(
                trace.traceId, trace.memoryId, trace.latencyMs,
                trace.estimatedQuestionTokens + trace.estimatedAnswerTokens,
                trace.success, trace.startTime));
        while (recentTraces.size() > RECENT_CACHE_SIZE) {
            recentTraces.pollLast();
        }
    }

    // ---------- 数据结构 ----------

    public static class Trace {
        public String traceId;
        public Long memoryId;
        public String question;
        public String startTime;
        public String endTime;
        public long startMillis;
        public long latencyMs;
        public int answerLength;
        public int estimatedQuestionTokens;
        public int estimatedAnswerTokens;
        public boolean success;
        public String error;
        public List<String> reflectionViolations = new ArrayList<>();
        public List<TraceEvent> events = new ArrayList<>();
    }

    public record TraceEvent(String stage, String timestamp, Map<String, Object> data) {
    }

    public record TraceSummary(String traceId, Long memoryId, long latencyMs,
                               int estimatedTokens, boolean success, String startTime) {
    }
}
