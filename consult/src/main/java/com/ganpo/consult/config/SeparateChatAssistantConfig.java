package com.ganpo.consult.config;

import com.ganpo.consult.store.MongoChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeparateChatAssistantConfig {

    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;

//这里有两个Bean,我把下面的给移掉了

//    @Bean
////    public ChatMemoryProvider chatMemoryProvider(){
//    ChatMemoryProvider chatMemoryProvider() {
//
//        return memoryId -> MessageWindowChatMemory
//                .builder()
//                .id(memoryId)
//                .maxMessages(10)
////                .chatMemoryStore(new InMemoryChatMemoryStore())
//                .chatMemoryStore(mongoChatMemoryStore)
//                .build();
//
//    }
}
