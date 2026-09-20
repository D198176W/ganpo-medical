package com.atguigu.consult.router;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 多模型路由器：按问题复杂度分级用模
 *
 * - PRIMARY（主模型 qwen-max 档）：复杂医疗场景 —— 长描述、症状分析、紧急情况、诊疗决策
 * - LITE（轻量模型 qwen-plus 档）：简单咨询、问候、FAQ —— 以更低 token 成本承接
 *
 * 规则引擎实现（可解释、零成本、可评测）；升级路径为轻量分类模型路由。
 */
@Component
public class ModelRouter {

    public enum ModelTier {PRIMARY, LITE}

    /** 复杂医疗场景关键词：命中即走主模型 */
    private static final Pattern COMPLEX_PATTERN = Pattern.compile(
            "症状|胸痛|胸闷|头痛|头晕|发热|发烧|腹痛|腹泻|出血|急救|紧急|诊断|治疗|用药|剂量|检查|报告|病史|并发|手术|心梗"
    );

    /** 超过该长度的描述性问题走主模型 */
    private static final int COMPLEX_LENGTH_THRESHOLD = 40;

    public ModelTier route(String question) {
        if (question == null || question.isBlank()) {
            return ModelTier.LITE;
        }
        String q = question.trim();
        if (q.length() > COMPLEX_LENGTH_THRESHOLD) {
            return ModelTier.PRIMARY;
        }
        if (COMPLEX_PATTERN.matcher(q).find()) {
            return ModelTier.PRIMARY;
        }
        return ModelTier.LITE;
    }
}
