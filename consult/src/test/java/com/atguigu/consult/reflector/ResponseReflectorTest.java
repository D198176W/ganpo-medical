package com.atguigu.consult.reflector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Reflector 自审规则单元测试
 */
class ResponseReflectorTest {

    private final ResponseReflector reflector = new ResponseReflector();

    @Test
    void compliantAnswerPasses() {
        String answer = "## 症状分析\n建议就诊呼吸内科。\n以上信息仅供参考，不能替代专业医疗诊断。";
        ResponseReflector.ReflectionResult result = reflector.reflect(answer);
        assertFalse(result.disclaimerMissing());
        assertFalse(result.hasCritical());
    }

    @Test
    void missingDisclaimerIsCritical() {
        String answer = "建议多喝水休息。";
        ResponseReflector.ReflectionResult result = reflector.reflect(answer);
        assertTrue(result.disclaimerMissing());
        assertTrue(result.hasCritical());
    }

    @Test
    void bannedHospitalIsCritical() {
        String answer = "推荐您去北京协和医院就诊。以上信息仅供参考。";
        ResponseReflector.ReflectionResult result = reflector.reflect(answer);
        assertTrue(result.violations().stream().anyMatch(v -> v.rule().equals("BANNED_HOSPITAL")));
    }

    @Test
    void definitiveDiagnosisIsWarning() {
        String answer = "你患了高血压，请注意休息。以上信息仅供参考。";
        ResponseReflector.ReflectionResult result = reflector.reflect(answer);
        assertTrue(result.violations().stream().anyMatch(v -> v.rule().equals("DEFINITIVE_DIAGNOSIS")));
        assertFalse(result.hasCritical());
    }

    @Test
    void dosageOutsideEmergencyIsWarning() {
        String answer = "可以口服布洛芬 300mg 缓解疼痛。以上信息仅供参考。";
        ResponseReflector.ReflectionResult result = reflector.reflect(answer);
        assertTrue(result.violations().stream().anyMatch(v -> v.rule().equals("DOSAGE_LEAK")));
    }

    @Test
    void dosageInEmergencyIsAllowed() {
        String answer = "🚨 紧急情况请立即拨打 120，可舌下含服硝酸甘油 0.5mg。以上信息仅供参考。";
        ResponseReflector.ReflectionResult result = reflector.reflect(answer);
        assertTrue(result.violations().stream().noneMatch(v -> v.rule().equals("DOSAGE_LEAK")));
    }

    @Test
    void emptyAnswerIsCritical() {
        ResponseReflector.ReflectionResult result = reflector.reflect("");
        assertTrue(result.hasCritical());
    }
}
