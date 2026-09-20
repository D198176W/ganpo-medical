package com.atguigu.consult.retrieval;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 意图感知路由分类器
 * 采用三层分类策略：规则引擎 + 关键词匹配 + 兜底默认
 *
 * 分类逻辑：
 * 1. 规则引擎：基于正则表达式匹配查询文本中的关键词模式
 * 2. 关键词匹配：对医疗领域高频词汇进行加权匹配
 * 3. 兜底：无法分类的查询归为通用查询
 */
@Component
public class QueryIntentClassifier {

    // 规则引擎：正则模式
    private static final Pattern HOSPITAL_PATTERN = Pattern.compile(
            ".*(医院|科室|挂号|预约|号源|门诊|急诊|住院|哪.*医院|推荐.*医院).*"
    );

    private static final Pattern DISEASE_PATTERN = Pattern.compile(
            ".*(什么.*病|.*是什么|.*原因|.*治疗|.*用药|.*症状|.*并发症|.*预防|.*检查).*"
    );

    private static final Pattern POLICY_PATTERN = Pattern.compile(
            ".*(医保|报销|政策|备案|定点|比例|上限|慢病|异地).*"
    );

    private static final Pattern SYMPTOM_PATTERN = Pattern.compile(
            ".*(头痛|头晕|发热|咳嗽|胸痛|腹痛|腹泻|恶心|呕吐|乏力|麻木|瘙痒|皮疹|视力.*下降|失眠|心慌|气短|胸闷).*"
    );

    /**
     * 对用户查询进行意图分类
     *
     * @param queryText 用户查询文本
     * @return 意图类型，用于决定检索权重
     */
    public QueryIntent classify(String queryText) {
        if (queryText == null || queryText.trim().isEmpty()) {
            return QueryIntent.GENERAL_QUERY;
        }

        String text = queryText.trim();

        // 第一层：规则引擎匹配
        // 政策查询优先级最高（医保相关词汇很独特，误判率低）
        if (POLICY_PATTERN.matcher(text).matches()) {
            return QueryIntent.POLICY_QUERY;
        }

        // 医院查询（包含医院/挂号/预约等关键词）
        if (HOSPITAL_PATTERN.matcher(text).matches()) {
            return QueryIntent.HOSPITAL_QUERY;
        }

        // 症状自查（用户直接描述自身症状）
        if (SYMPTOM_PATTERN.matcher(text).matches()) {
            return QueryIntent.SYMPTOM_QUERY;
        }

        // 疾病科普（询问疾病知识）
        if (DISEASE_PATTERN.matcher(text).matches()) {
            return QueryIntent.DISEASE_QUERY;
        }

        // 第二层：兜底为通用查询
        return QueryIntent.GENERAL_QUERY;
    }
}
