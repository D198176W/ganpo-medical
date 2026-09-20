package com.atguigu.consult.skills;

import com.atguigu.consult.retrieval.QueryIntent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;

/**
 * Skills 技能体系管理器
 *
 * 将"方法论/流程性知识"（症状分诊、就医导引、政策咨询、疾病科普）封装为可加载的
 * Skill 文档，应用启动时加载到内存，由意图路由按需选择并注入检索上下文，
 * 与 Tool（动作能力）形成互补：Tool 负责"做"，Skill 负责"怎么做"。
 *
 * 新增技能只需在 resources/skills/ 下增加 md 文件并在 SKILL_MAPPING 中注册，无需改动调用方。
 */
@Component
public class SkillManager {

    private static final Logger log = LoggerFactory.getLogger(SkillManager.class);

    /** 意图 -> 技能文档路径 映射 */
    private static final Map<QueryIntent, String> SKILL_MAPPING = new EnumMap<>(QueryIntent.class);

    static {
        SKILL_MAPPING.put(QueryIntent.SYMPTOM_QUERY, "classpath:skills/symptom_triage.md");
        SKILL_MAPPING.put(QueryIntent.HOSPITAL_QUERY, "classpath:skills/hospital_guide.md");
        SKILL_MAPPING.put(QueryIntent.POLICY_QUERY, "classpath:skills/policy_guide.md");
        SKILL_MAPPING.put(QueryIntent.DISEASE_QUERY, "classpath:skills/disease_education.md");
        SKILL_MAPPING.put(QueryIntent.GENERAL_QUERY, "classpath:skills/general.md");
    }

    private final Map<QueryIntent, String> skillContents = new EnumMap<>(QueryIntent.class);

    private final ResourcePatternResolver resourcePatternResolver;

    public SkillManager(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @PostConstruct
    public void init() {
        SKILL_MAPPING.forEach((intent, path) -> {
            try {
                Resource resource = resourcePatternResolver.getResource(path);
                if (resource.exists()) {
                    String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                    skillContents.put(intent, content);
                    log.info("Skill 加载成功: {} <- {}", intent, path);
                } else {
                    log.warn("Skill 文档不存在: {}", path);
                }
            } catch (Exception e) {
                log.warn("Skill 加载失败: {} - {}", path, e.getMessage());
            }
        });
        log.info("Skills 体系初始化完成: 已加载 {}/{} 个技能", skillContents.size(), SKILL_MAPPING.size());
    }

    /**
     * 按意图获取对应技能文档内容；未命中时返回通用技能
     */
    public String getSkillForIntent(QueryIntent intent) {
        return skillContents.getOrDefault(intent, skillContents.get(QueryIntent.GENERAL_QUERY));
    }

    public int getLoadedSkillCount() {
        return skillContents.size();
    }
}
