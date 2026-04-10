package com.atguigu.consult.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

@AiService(
        chatMemoryProvider = "chatMemoryProviderXiaozhi",
        streamingChatModel = "qwenStreamingChatModel",
        tools = "appointmentTools",
//        contentRetriever = "contentRetrieverXiaozhi"
        contentRetriever = "contentRetrieverXiaozhiPinecone"

)
//@AiService(wiringMode = WiringMode.EXPLICIT, chatMemoryProvider = "chatMemoryProviderXiaozhi")
public interface XiaozhiAgent {

<<<<<<< HEAD
    @SystemMessage(fromResource = "ganpo-prompt-template.txt")
=======
    @SystemMessage(fromResource = "ganpo-prompt-template1.txt")
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
  Flux <String> chat(@MemoryId Long memory, @UserMessage String usermessage);

}
