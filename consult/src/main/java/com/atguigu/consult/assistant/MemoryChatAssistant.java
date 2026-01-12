package com.atguigu.consult.assistant;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

/**
 * 初级智能体
 */
@AiService(chatMemory="chatMemory")
public interface MemoryChatAssistant {
    @UserMessage("你是我的好朋友，请用上海话回答问题。 {{message}}")

    String chat(@V("message") String message);
}
