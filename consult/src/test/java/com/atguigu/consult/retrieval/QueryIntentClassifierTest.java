package com.atguigu.consult.retrieval;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 意图分类器单元测试
 */
class QueryIntentClassifierTest {

    private final QueryIntentClassifier classifier = new QueryIntentClassifier();

    @Test
    void hospitalQuery() {
        assertEquals(QueryIntent.HOSPITAL_QUERY, classifier.classify("南昌大学第一附属医院怎么挂号"));
        assertEquals(QueryIntent.HOSPITAL_QUERY, classifier.classify("帮我预约呼吸内科的号"));
    }

    @Test
    void policyQuery() {
        assertEquals(QueryIntent.POLICY_QUERY, classifier.classify("大学生医保报销比例是多少"));
        assertEquals(QueryIntent.POLICY_QUERY, classifier.classify("异地就医怎么备案"));
    }

    @Test
    void symptomQuery() {
        assertEquals(QueryIntent.SYMPTOM_QUERY, classifier.classify("我最近总是头痛"));
        assertEquals(QueryIntent.SYMPTOM_QUERY, classifier.classify("胸闷气短怎么办"));
    }

    @Test
    void diseaseQuery() {
        assertEquals(QueryIntent.DISEASE_QUERY, classifier.classify("高血压是什么原因导致的"));
        assertEquals(QueryIntent.DISEASE_QUERY, classifier.classify("流感怎么治疗"));
    }

    @Test
    void generalQuery() {
        assertEquals(QueryIntent.GENERAL_QUERY, classifier.classify("你好"));
        assertEquals(QueryIntent.GENERAL_QUERY, classifier.classify(""));
        assertEquals(QueryIntent.GENERAL_QUERY, classifier.classify(null));
    }
}
