package com.atguigu.consult.store;

import com.atguigu.consult.bean.ChatMessages;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;


@Component
public class MongoChatMemoryStore implements ChatMemoryStore {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<dev.langchain4j.data.message.ChatMessage> getMessages(Object memoryId) {
//        Criteria criteria = Criteria.where("memoryId").is("memoryId");
        Criteria criteria = Criteria.where("memoryId").is(memoryId.toString());
        Query query = new Query(criteria);
        ChatMessages chatMessages = mongoTemplate.findOne(query, ChatMessages.class);
//        return null;
        if(chatMessages ==null){
            return new LinkedList<>();
        }
//        String contentJson = chatMessages.getContent();
//        return ChatMessageDeserializer.messagesFromJson(contentJson);
        return ChatMessageDeserializer.messagesFromJson(chatMessages.getContent());


    }

    @Override
    public void updateMessages(Object memoryId, List<dev.langchain4j.data.message.ChatMessage> messages) {
//    public void updateMessages(Object o, List<ChatMessages> list) {
//        Criteria criteria = Criteria.where("memoryId").is("memoryId");
        Criteria criteria = Criteria.where("memoryId").is(memoryId.toString());
        Query query = new Query(criteria);
        Update update = new Update();
//        update.set("content", ChatMessageSerializer.messagesToJson(list));
        update.set("content", ChatMessageSerializer.messagesToJson(messages));
        update.set("memoryId",memoryId.toString());
        //修改或者新增
        mongoTemplate.upsert(query,update, ChatMessages.class);

    }

    @Override
    public void deleteMessages(Object memoryId) {
        Criteria criteria = Criteria.where("memoryId").is(memoryId);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, ChatMessages.class);

    }
}
