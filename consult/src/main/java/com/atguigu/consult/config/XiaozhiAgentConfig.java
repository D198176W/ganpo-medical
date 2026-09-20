package com.atguigu.consult.config;


//import com.atguigu.javaailangchain4j.store.MongoChatMemoryStore;
import com.atguigu.consult.store.MongoChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XiaozhiAgentConfig {

    /**
     * 上下文窗口 token 预算：超出后按 token 计数截断最旧消息（而非按消息条数），
     * 防止长对话 + RAG 注入超出模型上下文窗口
     */
    private static final int MAX_MEMORY_TOKENS = 3000;

    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;

    @Bean
    public ChatMemoryProvider chatMemoryProviderXiaozhi(){
        Tokenizer tokenizer = new OpenAiTokenizer("gpt-4");
        return memoryId ->
                TokenWindowChatMemory.builder()
                        .id(memoryId)
                        .maxTokens(MAX_MEMORY_TOKENS, tokenizer)
                        .chatMemoryStore(mongoChatMemoryStore)
                        .build();
    }

    // 检索能力统一由 HybridContentRetriever 提供（Pinecone + 本地向量 + BM25 三路混合检索）

}
