package com.atguigu.consult.review;

import com.atguigu.consult.assistant.RiskReviewAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 风控审核服务：调用风控审核 Agent（红队）复核主回答
 *
 * 与规则版 Reflector 形成双层防护：
 * - Reflector（规则引擎）：零成本、可解释，前置实时检测
 * - RiskReviewAgent（LLM 红队）：语义级审核，兜底规则覆盖不到的场景
 *
 * 审核失败（模型不可用、超时）时降级为 PASS，不阻断主流程。
 */
@Service
public class SafetyReviewService {

    private static final Logger log = LoggerFactory.getLogger(SafetyReviewService.class);

    /** BLOCK 判定后向用户补发的安全提示 */
    public static final String BLOCK_NOTICE =
            "\n\n⚠️ **安全提示**：以上内容经安全审核发现可能存在风险，请务必以线下医生面诊意见为准；如有紧急症状请立即拨打 120 🏥";

    private static final Pattern VERDICT_PATTERN = Pattern.compile("VERDICT:\\s*(PASS|WARN|BLOCK)", Pattern.CASE_INSENSITIVE);
    private static final Pattern ISSUES_PATTERN = Pattern.compile("ISSUES:\\s*(.+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    @Autowired
    private RiskReviewAgent riskReviewAgent;

    @Value("${app.review.enabled:true}")
    private boolean reviewEnabled;

    /** 短回答（问候、确认类）跳过审核以控制成本 */
    @Value("${app.review.min-answer-length:80}")
    private int minAnswerLength;

    public boolean shouldReview(String answer) {
        return reviewEnabled && answer != null && answer.length() >= minAnswerLength;
    }

    public ReviewResult review(String question, String answer) {
        try {
            String raw = riskReviewAgent.review(question, answer);
            return parse(raw);
        } catch (Exception e) {
            // 审核服务故障降级：记录日志，不阻断主流程
            log.warn("风控审核调用失败，降级为 PASS: {}", e.getMessage());
            return new ReviewResult("PASS", "审核服务降级: " + e.getMessage());
        }
    }

    private ReviewResult parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return new ReviewResult("PASS", "审核返回为空");
        }
        String verdict = "PASS";
        Matcher vm = VERDICT_PATTERN.matcher(raw);
        if (vm.find()) {
            verdict = vm.group(1).toUpperCase();
        }
        String issues = "";
        Matcher im = ISSUES_PATTERN.matcher(raw);
        if (im.find()) {
            issues = im.group(1).trim();
        }
        log.info("风控审核结果: {} | {}", verdict, issues);
        return new ReviewResult(verdict, issues);
    }

    public record ReviewResult(String verdict, String issues) {
        public boolean isBlock() {
            return "BLOCK".equalsIgnoreCase(verdict);
        }
    }
}
