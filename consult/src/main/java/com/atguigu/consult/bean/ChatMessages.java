package com.atguigu.consult.bean;


import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("chat_messages")
public class ChatMessages implements ChatMessage {

    //唯一标识，映射到mongoda中的_id字段,这里的id注解要注意导入springframework的包，而不是其他的Id

//    private Long messageId;
    @Id
//    private ObjectId messageId;
//
//    private String memoryId;
    private ObjectId id;

    private int messageId;

//    存储当前聊天列表的json字符串
    private String content;

    private String memoryId;

    // 示例：ChatMessage类中应有
    public String getContent() { return content; } // 若代码中用了getContent()

    @Override
    public ChatMessageType type() {
        return null;
    }

    public String content() {
        return content;
    }
//    public String getText() { return text; }       // 若代码中用了getText()


}
