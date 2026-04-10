package com.atguigu.consult.tools;


import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTools {

    @Tool(name = "加法运算",value = "计算两个数的和")
    double sum(
            @ToolMemoryId int memoryId,

           @P(value = "加数1",required = true) double a,

           @P(value = "加数2",required = true)double b
    )
            {
        System.out.println("调用加法运算" + memoryId);
        return a+b;
    }

    @Tool(name = "平方根计算",value = "计算一个数的平方根")
    double squareRoot( @ToolMemoryId int memoryId,double a){
        System.out.println("调用平方根运算" + memoryId);
        return Math.sqrt(a);
    }

}
