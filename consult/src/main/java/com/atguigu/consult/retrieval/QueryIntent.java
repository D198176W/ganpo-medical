package com.atguigu.consult.retrieval;

/**
 * 查询意图枚举
 * 用于意图感知路由，根据不同意图动态调整向量检索与关键词检索的权重
 */
public enum QueryIntent {

    /**
     * 医院查询：如"南昌大学第一附属医院在哪""呼吸内科哪个医院好"
     * 关键词精确匹配更重要（医院名称、科室名称需要精确命中）
     */
    HOSPITAL_QUERY("医院查询", 0.3, 0.7),

    /**
     * 疾病科普：如"头痛可能是什么原因""高血压怎么治疗"
     * 向量语义检索更重要（症状描述多样，需要语义理解）
     */
    DISEASE_QUERY("疾病科普", 0.7, 0.3),

    /**
     * 政策咨询：如"大学生医保报销比例""异地就医怎么备案"
     * 均衡策略，关键词和语义都重要
     */
    POLICY_QUERY("政策咨询", 0.5, 0.5),

    /**
     * 症状自查：如"我最近总是头晕""胸口有点闷"
     * 向量语义检索为主（用户描述模糊，需要语义匹配）
     */
    SYMPTOM_QUERY("症状自查", 0.8, 0.2),

    /**
     * 通用查询：无法明确分类的查询
     * 均衡策略
     */
    GENERAL_QUERY("通用查询", 0.5, 0.5);

    private final String description;
    private final double vectorWeight;   // 向量检索权重
    private final double keywordWeight;  // 关键词检索权重

    QueryIntent(String description, double vectorWeight, double keywordWeight) {
        this.description = description;
        this.vectorWeight = vectorWeight;
        this.keywordWeight = keywordWeight;
    }

    public String getDescription() {
        return description;
    }

    public double getVectorWeight() {
        return vectorWeight;
    }

    public double getKeywordWeight() {
        return keywordWeight;
    }
}
