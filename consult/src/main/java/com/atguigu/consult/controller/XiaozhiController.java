package com.atguigu.consult.controller;




import com.atguigu.consult.assistant.XiaozhiAgent;
import com.atguigu.consult.assistant.XiaozhiLiteAgent;
import com.atguigu.consult.bean.ChatForm;
import com.atguigu.consult.bean.ChatMessages;
import com.atguigu.consult.cache.ChatCacheService;
import com.atguigu.consult.dto.MessageDTO;
import com.atguigu.consult.reflector.ResponseReflector;
import com.atguigu.consult.retrieval.QueryIntentClassifier;
import com.atguigu.consult.review.SafetyReviewService;
import com.atguigu.consult.router.ModelRouter;
import com.atguigu.consult.store.MongoChatMemoryStore;
import com.atguigu.consult.trace.TraceService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Tag(name="赣鄱医枢")


@RestController
@RequestMapping("/xiaozhi")
public class XiaozhiController {

    @Autowired
    private XiaozhiAgent xiaozhiAgent;

    @Autowired
    private XiaozhiLiteAgent xiaozhiLiteAgent;

    @Autowired
    private ModelRouter modelRouter;

    @Autowired
    private SafetyReviewService safetyReviewService;

    @Autowired
    private ChatCacheService chatCacheService;

    @Autowired
    private ResponseReflector responseReflector;

    @Autowired
    private TraceService traceService;

    @Autowired
    private QueryIntentClassifier intentClassifier;

    @Operation(summary = "对话")
    @PostMapping(value = "/chat", produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatForm chatForm) {
        // 参数校验
        if (chatForm == null) {
            return Flux.just("请求参数不能为空");
        }

        if (chatForm.getMessage() == null || chatForm.getMessage().trim().isEmpty()) {
            return Flux.just("消息内容不能为空");
        }

        // 确保memoryId不为空，前端可能没有提供
        Long memoryId = chatForm.getMemoryId();
        if (memoryId == null) {
            // 生成新的memoryId
            memoryId = System.currentTimeMillis();
        }

        try {
            // 开启全链路 Trace
            TraceService.Trace trace = traceService.beginTrace(memoryId, chatForm.getMessage());
            traceService.recordEvent(trace, "intent",
                    Map.of("intent", intentClassifier.classify(chatForm.getMessage()).name()));

            // Unified Cache：仅对无历史上下文的首次提问（FAQ 场景）启用缓存，避免多轮对话上下文污染
            boolean cacheable = mongoChatMemoryStore.getMessages(memoryId).isEmpty();
            if (cacheable) {
                String cacheKey = chatCacheService.buildCacheKey(chatForm.getMessage());
                String cached = chatCacheService.getCachedAnswer(cacheKey);
                if (cached != null) {
                    chatCacheService.recordHit();
                    traceService.recordEvent(trace, "cache", Map.of("hit", true));
                    traceService.endTrace(trace, cached, List.of());
                    return Flux.just(cached);
                }
                chatCacheService.recordMiss();
                traceService.recordEvent(trace, "cache", Map.of("hit", false));
                // 模型路由 + 流式透传 + 旁路累积 + Reflector 自审 + 风控审核 + Trace 收尾，完成后写回缓存
                StringBuilder answerBuffer = new StringBuilder();
                return withReflection(chatWithRouting(memoryId, chatForm.getMessage(), trace),
                        chatForm.getMessage(), answerBuffer, trace)
                        .doOnComplete(() -> chatCacheService.cacheAnswer(cacheKey, answerBuffer.toString()));
            }

            return withReflection(chatWithRouting(memoryId, chatForm.getMessage(), trace),
                    chatForm.getMessage(), new StringBuilder(), trace);
        } catch (Exception e) {
            return Flux.error(new RuntimeException("对话处理失败: " + e.getMessage()));
        }
    }

    /**
     * 多模型路由 + 故障降级：
     * 按问题复杂度选择主/轻量模型（成本分级）；首选模型在尚未输出任何内容时故障，
     * 自动降级到另一档模型重试（已有输出则不降级，避免重复前缀）
     */
    private Flux<String> chatWithRouting(Long memoryId, String message, TraceService.Trace trace) {
        ModelRouter.ModelTier tier = modelRouter.route(message);
        traceService.recordEvent(trace, "model-routing", Map.of("tier", tier.name()));

        if (tier == ModelRouter.ModelTier.LITE) {
            return withFallback(() -> xiaozhiLiteAgent.chat(memoryId, message),
                    () -> xiaozhiAgent.chat(memoryId, message), trace, "lite->primary");
        }
        return withFallback(() -> xiaozhiAgent.chat(memoryId, message),
                () -> xiaozhiLiteAgent.chat(memoryId, message), trace, "primary->lite");
    }

    private Flux<String> withFallback(Supplier<Flux<String>> primary, Supplier<Flux<String>> fallback,
                                      TraceService.Trace trace, String direction) {
        AtomicBoolean emitted = new AtomicBoolean(false);
        return Flux.defer(primary)
                .doOnNext(s -> emitted.set(true))
                .onErrorResume(e -> {
                    if (emitted.get()) {
                        return Flux.error(e);
                    }
                    traceService.recordEvent(trace, "model-fallback",
                            Map.of("direction", direction, "reason", String.valueOf(e.getMessage())));
                    return Flux.defer(fallback);
                });
    }

    /**
     * 流式回答 + Reflector 自审 + 风控审核 + Trace 收尾：
     * 旁路累积完整回答；流结束后先由规则版 Reflector 做合规自审（缺失免责声明自动补发），
     * 再由风控审核 Agent（LLM 红队）做语义级复核（BLOCK 时补发安全提示）；
     * Trace 记录时延与 token 估算
     */
    private Flux<String> withReflection(Flux<String> stream, String question,
                                        StringBuilder answerBuffer, TraceService.Trace trace) {
        List<String> violations = Collections.synchronizedList(new ArrayList<>());
        Flux<String> accumulated = stream.doOnNext(answerBuffer::append);
        Flux<String> correction = Flux.defer(() -> {
            ResponseReflector.ReflectionResult result = responseReflector.reflect(answerBuffer.toString());
            result.violations().forEach(v -> violations.add(v.rule() + ": " + v.detail()));
            if (!violations.isEmpty()) {
                traceService.recordEvent(trace, "reflection", Map.of("violations", violations));
            }
            if (result.disclaimerMissing()) {
                return Flux.just(ResponseReflector.DISCLAIMER);
            }
            return Flux.empty();
        });
        // 风控审核（红队 Agent）：阻塞式 LLM 调用放到弹性线程池，不占用事件循环
        Flux<String> safetyReview = Flux.defer(() -> {
            if (!safetyReviewService.shouldReview(answerBuffer.toString())) {
                return Flux.empty();
            }
            return Mono.fromCallable(() -> safetyReviewService.review(question, answerBuffer.toString()))
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMapMany(result -> {
                        traceService.recordEvent(trace, "safety-review",
                                Map.of("verdict", result.verdict(), "issues", result.issues()));
                        if (result.isBlock()) {
                            return Flux.just(SafetyReviewService.BLOCK_NOTICE);
                        }
                        return Flux.empty();
                    })
                    .onErrorResume(e -> Flux.empty());
        });
        return Flux.concat(accumulated, correction, safetyReview)
                .doOnComplete(() -> traceService.endTrace(trace, answerBuffer.toString(), violations))
                .doOnError(e -> traceService.endTraceWithError(trace, e.getMessage()))
                .doOnCancel(() -> traceService.endTraceWithError(trace, "客户端取消"));
    }

    @Operation(summary = "缓存命中率统计")
    @GetMapping("/cache/stats")
    public ChatCacheService.CacheStats cacheStats() {
        return chatCacheService.getStats();
    }

    @Operation(summary = "重置缓存统计")
    @DeleteMapping("/cache/stats")
    public String resetCacheStats() {
        chatCacheService.resetStats();
        return "缓存统计已重置";
    }

    @Operation(summary = "最近对话 Trace 摘要（可观测性）")
    @GetMapping("/trace/recent")
    public List<TraceService.TraceSummary> recentTraces() {
        return traceService.getRecentTraces();
    }





    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;



    @Operation(summary = "获取聊天历史")
    @GetMapping("/history/{memoryId}")
    public List<MessageDTO> getHistory(@PathVariable Long memoryId) {
        if (memoryId == null) {
            return new ArrayList<MessageDTO>();
        }

        try {
            List<ChatMessage> chatMessage = mongoChatMemoryStore.getMessages(memoryId);
            if (chatMessage == null) {
                chatMessage = new ArrayList<>();
            }

            return chatMessage.stream().map(c -> {
                ChatMessages chatMessages = (ChatMessages) c;
                MessageDTO dto = new MessageDTO();
                dto.setId(UUID.randomUUID().toString());
                dto.setSender(c.type() == ChatMessageType.USER ? "user" : "bot");
                dto.setText(chatMessages.content());
//                dto.setText(chatMessage.text());


                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<MessageDTO>();
        }
    }


    // 新增接口：清除聊天历史
    @Operation(summary = "清除聊天历史")
    @DeleteMapping("/history/{memoryId}")
    public String clearHistory(@PathVariable Long memoryId) {
        if (memoryId == null) {
            return "memoryId 不能为空";
        }
        mongoChatMemoryStore.deleteMessages(memoryId);
        return "历史记录已清除 - memoryId: " + memoryId;
    }


//    @PostMapping("/voice-to-text")
//    public String voiceToText(@RequestParam("file") MultipartFile file) {
//        // 调用语音识别API转换为文字
//        return voiceRecognitionService.recognize(file);
//    }


}

