package com.atguigu.healthmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 健康报告实体类
 * 对应数据库表：health_reports
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("health_reports")
public class HealthReport {

    /**
     * 健康评分等级枚举
     */
    /**
     * 健康评分等级枚举
     */
    public enum HealthGrade {
        EXCELLENT("excellent"),  // 优秀 (90-100分)
        GOOD("good"),           // 良好 (80-89分)
        NORMAL("normal"),       // 一般 (70-79分)
        POOR("poor"),           // 较差 (60-69分)
        BAD("bad");             // 差 (60分以下)

        @EnumValue
        private final String code;

        HealthGrade(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * 报告状态枚举
     */
    public enum ReportStatus {
        DRAFT("draft"),          // 草稿
        GENERATED("generated"),  // 已生成
        PUBLISHED("published"),  // 已发布
        ARCHIVED("archived");    // 已归档

        @EnumValue
        private final String code;

        ReportStatus(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * 报告类型枚举
     */
    public enum ReportType {
        DAILY("daily"),      // 日报
        WEEKLY("weekly"),     // 周报
        MONTHLY("monthly"),    // 月报
        QUARTERLY("quarterly"),  // 季报
        YEARLY("yearly"),     // 年报
        CUSTOM("custom");    // 自定义报告

        @EnumValue
        private final String code;
        ReportType(String code){
            this.code=code;
        }
        public String getCode(){
            return this.code;
        }
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 报告标题
     */
    @TableField("title")
    private String title;

    /**
     * 报告类型
     */
    @TableField("report_type")
    private ReportType reportType;

    /**
     * 报告开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 报告结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 总体健康评分（0-100）
     */
    @TableField("overall_score")
    private Integer overallScore;

    /**
     * 健康等级
     */
    @TableField("health_grade")
    private HealthGrade healthGrade;

    /**
     * 睡眠评分
     */
    @TableField("sleep_score")
    private Integer sleepScore;

    /**
     * 运动评分
     */
    @TableField("exercise_score")
    private Integer exerciseScore;

    /**
     * 饮食评分
     */
    @TableField("diet_score")
    private Integer dietScore;

    /**
     * 心理状态评分
     */
    @TableField("mental_score")
    private Integer mentalScore;

    /**
     * 体重变化（单位：kg，正数表示增加，负数表示减少）
     */
    @TableField("weight_change")
    private Double weightChange;

    /**
     * 血压（收缩压）
     */
    @TableField("blood_pressure_high")
    private Integer bloodPressureHigh;

    /**
     * 血压（舒张压）
     */
    @TableField("blood_pressure_low")
    private Integer bloodPressureLow;

    /**
     * 心率（次/分钟）
     */
    @TableField("heart_rate")
    private Integer heartRate;

    /**
     * 报告状态
     */
    @TableField("report_status")
    private ReportStatus reportStatus;

    /**
     * 报告内容摘要（JSON格式存储详细分析结果）
     */
    @TableField("summary_content")
    private String summaryContent;

    /**
     * 详细分析内容（JSON格式）
     */
    @TableField("detail_content")
    private String detailContent;

    /**
     * 医生建议
     */
    @TableField("doctor_advice")
    private String doctorAdvice;

    /**
     * 生成报告的用户ID（可能是医生或系统）
     */
    @TableField("generated_by")
    private Long generatedBy;

    /**
     * 报告生成时间
     */
    @TableField("generated_at")
    private LocalDateTime generatedAt;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除字段（0-未删除，1-已删除）
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 构造函数 - 生成报告时使用
     */
    public HealthReport(Long userId, String title, ReportType reportType,
                        LocalDate startDate, LocalDate endDate) {
        this.userId = userId;
        this.title = title;
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportStatus = ReportStatus.DRAFT;
        this.generatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deleted = 0; // 明确初始化为未删除
    }

    /**
     * 默认构造函数
     */
    public HealthReport() {
        // MyBatis Plus需要无参构造函数
    }

    /**
     * 计算并设置健康等级
     */
    public void calculateHealthGrade() {
        if (overallScore == null) return;

        if (overallScore >= 90) {
            this.healthGrade = HealthGrade.EXCELLENT;
        } else if (overallScore >= 80) {
            this.healthGrade = HealthGrade.GOOD;
        } else if (overallScore >= 70) {
            this.healthGrade = HealthGrade.NORMAL;
        } else if (overallScore >= 60) {
            this.healthGrade = HealthGrade.POOR;
        } else {
            this.healthGrade = HealthGrade.BAD;
        }
    }

    /**
     * 计算总体评分（基于各维度评分加权平均）
     */
    public void calculateOverallScore() {
        if (sleepScore == null || exerciseScore == null || dietScore == null || mentalScore == null) {
            return;
        }

        // 加权平均计算总体评分
        double weightedScore = (sleepScore * 0.3) + (exerciseScore * 0.25) +
                (dietScore * 0.25) + (mentalScore * 0.2);
        this.overallScore = (int) Math.round(weightedScore);
        calculateHealthGrade();
    }

    /**
     * 判断血压是否正常
     */
    public boolean isBloodPressureNormal() {
        if (bloodPressureHigh == null || bloodPressureLow == null) {
            return false;
        }
        return bloodPressureHigh >= 90 && bloodPressureHigh <= 140 &&
                bloodPressureLow >= 60 && bloodPressureLow <= 90;
    }

    /**
     * 判断心率是否正常
     */
    public boolean isHeartRateNormal() {
        if (heartRate == null) return false;
        return heartRate >= 60 && heartRate <= 100;
    }

    /**
     * 生成报告摘要
     */
    public String generateSummary() {
        if (overallScore == null || healthGrade == null) {
            return "报告数据不完整，无法生成摘要";
        }

        StringBuilder summary = new StringBuilder();
        summary.append(String.format("在%s至%s期间，您的总体健康评分为%d分，等级为%s。",
                startDate, endDate, overallScore, getHealthGradeDescription()));

        if (sleepScore != null) {
            summary.append(String.format("睡眠评分：%d分。", sleepScore));
        }
        if (exerciseScore != null) {
            summary.append(String.format("运动评分：%d分。", exerciseScore));
        }

        return summary.toString();
    }

    /**
     * 获取健康等级描述
     */
    private String getHealthGradeDescription() {
        switch (healthGrade) {
            case EXCELLENT: return "优秀";
            case GOOD: return "良好";
            case NORMAL: return "一般";
            case POOR: return "较差";
            case BAD: return "差";
            default: return "未知";
        }
    }

    /**
     * 获取报告类型描述
     */
    public String getReportTypeDescription() {
        switch (reportType) {
            case DAILY: return "日报";
            case WEEKLY: return "周报";
            case MONTHLY: return "月报";
            case QUARTERLY: return "季报";
            case YEARLY: return "年报";
            case CUSTOM: return "自定义报告";
            default: return "未知";
        }
    }

    /**
     * 验证报告数据的完整性
     */
    public boolean isValid() {
        return userId != null && title != null && !title.trim().isEmpty() &&
                reportType != null && startDate != null && endDate != null &&
                !startDate.isAfter(endDate);
    }

    public Long getReportId() {
        return this.id;
    }

    public LocalDateTime getGeneratedat() {
        return this.generatedAt;
    }

    public String getStatus(){
        return this.reportStatus != null ? this.reportStatus.name():null;
    }
}
