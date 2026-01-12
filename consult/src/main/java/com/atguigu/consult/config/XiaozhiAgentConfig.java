package com.atguigu.consult.config;


//import com.atguigu.javaailangchain4j.store.MongoChatMemoryStore;
import com.atguigu.consult.store.MongoChatMemoryStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class XiaozhiAgentConfig {

    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;

    @Bean
    public ChatMemoryProvider chatMemoryProviderXiaozhi(){
        return memoryId ->
                MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .chatMemoryStore(mongoChatMemoryStore)
                        .build();
    }

//    @Bean
//
//    ContentRetriever contentRetrieverXiaozhi(){
////        我真的是有点服了这里的自动提示，莫名奇妙的在文件名的定义时多加上一个.,比如医院信息就给我写成了.医院信息.md
//        Document document1 = FileSystemDocumentLoader.loadDocument("E:\\DW project sources\\xiaozhi medical\\knowledge\\医院信息.md");
//        Document document2 = FileSystemDocumentLoader.loadDocument("E:\\DW project sources\\xiaozhi medical\\knowledge\\科室信息.md");
//        Document document3 = FileSystemDocumentLoader.loadDocument("E:\\DW project sources\\xiaozhi medical\\knowledge\\神经内科.md");
//        List<Document> documents = Arrays.asList(document1,document2,document3);
//
//        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
//
//        EmbeddingStoreIngestor.ingest(documents,embeddingStore);
//
//        return EmbeddingStoreContentRetriever.from(embeddingStore);
//
//
//    }


    @Autowired
    private EmbeddingStore embeddingStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Bean
    ContentRetriever contentRetrieverXiaozhiPinecone(){
        return EmbeddingStoreContentRetriever
                .builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(1)
                .minScore(0.8)
                .build();
    }

}
