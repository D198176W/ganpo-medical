package com.atguigu.consult.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

/**
 * 风控审核 Agent（红队）：独立于主问诊 Agent，对主回答做医疗安全复核。
 * 与主 Agent 形成"分析-审核"双 Agent 协作结构，
 * 审核维度：用药安全、诊断越界、急救处置、地域合规、幻觉、免责声明。
 */
@AiService(chatModel = "qwenChatModel")
public interface RiskReviewAgent {

    @SystemMessage(fromResource = "risk-review-prompt.txt")
    @UserMessage("【用户问题】\n{{question}}\n\n【助手回答】\n{{answer}}\n\n请输出审核结论。")
    String review(@V("question") String question, @V("answer") String answer);

}
