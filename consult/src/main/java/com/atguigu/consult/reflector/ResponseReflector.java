package com.atguigu.consult.reflector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Reflector：回答自审与合规修正
 *
 * 对模型输出做"带诊断结论的回检"（区别于原样重试）：
 * 1. 免责声明缺失检测 —— 医疗场景硬性合规要求，缺失则自动补发
 * 2. 违禁内容检测 —— 外地医院（北京协和医院）、确诊性表述
 * 3. 用药安全检测 —— 非紧急场景下出现具体剂量（紧急场景的急救剂量属允许范围）
 *
 * 采用规则引擎实现：可解释、零成本、可评测；已知边界是同义词与反讽，
 * 升级路径为 Embedding 相似度 / NLI 语义校验。
 */
@Component
public class ResponseReflector {

    private static final Logger log = LoggerFactory.getLogger(ResponseReflector.class);

    public static final String DISCLAIMER =
            "\n\n---\n以上信息仅供参考，不能替代专业医疗诊断。如症状加重，请及时就医 🏥";

    private static final Pattern DISCLAIMER_PATTERN =
            Pattern.compile("仅供参考|遵医嘱|替代专业");

    private static final Pattern BANNED_HOSPITAL_PATTERN =
            Pattern.compile("北京协和医院");

    private static final Pattern DEFINITIVE_DIAGNOSIS_PATTERN =
            Pattern.compile("确诊(为|了)|你(患|得)了|您(患|得)了");

    private static final Pattern DOSAGE_PATTERN =
            Pattern.compile("\\d+(\\.\\d+)?\\s*(mg|毫克|ml|毫升|克|片|粒)");

    private static final Pattern EMERGENCY_PATTERN =
            Pattern.compile("紧急|急救|拨打\\s*120|心梗|休克");

    public enum Severity {INFO, WARNING, CRITICAL}

    public record Violation(Severity severity, String rule, String detail) {
    }

    public record ReflectionResult(List<Violation> violations, boolean disclaimerMissing) {

        public boolean hasCritical() {
            return violations.stream().anyMatch(v -> v.severity() == Severity.CRITICAL);
        }
    }

    /**
     * 对完整回答做自审，返回诊断结果（不修改回答本身，修正动作由调用方决定）
     */
    public ReflectionResult reflect(String answer) {
        List<Violation> violations = new ArrayList<>();
        if (answer == null || answer.isBlank()) {
            violations.add(new Violation(Severity.CRITICAL, "EMPTY_ANSWER", "回答为空"));
            return new ReflectionResult(violations, true);
        }

        // 1. 免责声明检测
        boolean disclaimerMissing = !DISCLAIMER_PATTERN.matcher(answer).find();
        if (disclaimerMissing) {
            violations.add(new Violation(Severity.CRITICAL, "DISCLAIMER_MISSING", "缺少免责声明"));
        }

        // 2. 违禁医院检测
        if (BANNED_HOSPITAL_PATTERN.matcher(answer).find()) {
            violations.add(new Violation(Severity.CRITICAL, "BANNED_HOSPITAL", "推荐了外地医院（北京协和医院）"));
        }

        // 3. 确诊性表述检测
        var m = DEFINITIVE_DIAGNOSIS_PATTERN.matcher(answer);
        if (m.find()) {
            violations.add(new Violation(Severity.WARNING, "DEFINITIVE_DIAGNOSIS",
                    "出现确诊性表述: " + m.group()));
        }

        // 4. 非紧急场景的具体剂量检测
        boolean isEmergency = EMERGENCY_PATTERN.matcher(answer).find();
        if (!isEmergency) {
            var dm = DOSAGE_PATTERN.matcher(answer);
            if (dm.find()) {
                violations.add(new Violation(Severity.WARNING, "DOSAGE_LEAK",
                        "非紧急场景出现具体剂量: " + dm.group()));
            }
        }

        if (!violations.isEmpty()) {
            log.info("Reflector 自审结果: {} 项违规 - {}", violations.size(), violations);
        }
        return new ReflectionResult(violations, disclaimerMissing);
    }
}
