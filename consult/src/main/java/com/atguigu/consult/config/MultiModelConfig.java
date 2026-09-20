package com.atguigu.consult.config;

import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多模型配置：主模型（由 DashScope Starter 自动装配 qwenStreamingChatModel）+ 轻量模型
 *
 * 成本分级策略：简单咨询/FAQ 走轻量模型降低 token 成本，
 * 复杂医疗场景（症状分析、紧急情况）走主模型保证质量；
 * 任一模型故障时自动降级到另一档（见 ModelRouter / XiaozhiController）。
 */
@Configuration
public class MultiModelConfig {

    @Bean("qwenLiteStreamingChatModel")
    public StreamingChatLanguageModel qwenLiteStreamingChatModel(
            @Value("${langchain4j.community.dashscope.streaming-chat-model.api-key}") String apiKey,
            @Value("${app.model.lite-model-name:qwen-plus}") String liteModelName) {
        return QwenStreamingChatModel.builder()
                .apiKey(apiKey)
                .modelName(liteModelName)
                .build();
    }
}
