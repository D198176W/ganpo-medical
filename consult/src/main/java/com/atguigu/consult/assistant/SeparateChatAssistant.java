package com.atguigu.consult.assistant;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

//@AiService(chatMemoryProvider = "chatMemoryProviderXiaozhi")
@AiService(
        chatMemoryProvider="chatMemoryProvider",
        tools = "calculatorTools"
)

public interface SeparateChatAssistant {

//    系统消息提示词
//    @SystemMessage("你是我的好朋友，请用东北话回答问题")
//    我真的是服了自己，date可以写成data
    @SystemMessage(fromResource = "my-prompt-template.txt")
//@SystemMessage("你是我的好朋友，请用江西话回答问题。今天是{{current_date}}")
    String chat(@MemoryId  int memoryId , @UserMessage String userMessage );

    @UserMessage("你是我的好朋友，请用粤语回答问题。{{message}}")
    String chat2(@MemoryId int memoryId, @V("message") String userMessage);

    @SystemMessage(fromResource = "my-prompt-template3.txt")
    String chat3(
            @MemoryId int memoryId,
            @UserMessage String userMessage,
            @V("username") String username,
            @V("age") int age
    );

}
