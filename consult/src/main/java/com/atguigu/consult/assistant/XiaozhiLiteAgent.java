package com.atguigu.consult.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

/**
 * 轻量模型 Agent：与主 Agent 共享记忆/工具/检索/提示词，
 * 仅模型档位不同，用于简单咨询场景的成本分级与主模型故障降级
 */
@AiService(
        chatMemoryProvider = "chatMemoryProviderXiaozhi",
        streamingChatModel = "qwenLiteStreamingChatModel",
        tools = "appointmentTools",
        contentRetriever = "hybridContentRetriever"
)
public interface XiaozhiLiteAgent {

    @SystemMessage(fromResource = "ganpo-prompt-template1.txt")
    Flux<String> chat(@MemoryId Long memory, @UserMessage String usermessage);

}
