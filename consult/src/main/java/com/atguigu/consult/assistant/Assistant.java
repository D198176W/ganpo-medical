package com.atguigu.consult.assistant;

import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

//@AiService(wiringMode = EXPLICIT,chatModel = "qwenChatModel")
//如果在配置文件里面多个模型，在使用@AiService的时候就需要指定对应模型的名称，但我已经把其他的模型都注释了，只保留了免费的千问
@AiService
public interface Assistant {
    String chat(String userMessage);
}
