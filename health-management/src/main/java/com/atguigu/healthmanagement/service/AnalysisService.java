package com.atguigu.healthmanagement.service;

import java.time.LocalDate;
import java.util.Map;

/**
 * 健康数据分析服务接口
 */
public interface AnalysisService {

    /**
     * 生成周度健康报告
     */
    Map<String, Object> generateWeeklyReport(Long userId, LocalDate date);

    /**
     * 生成月度健康报告
     */
    Map<String, Object> generateMonthlyReport(Long userId, int year, int month);

    /**
     * 睡眠质量分析
     */
    Map<String, Object> analyzeSleepQuality(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 运动习惯分析
     */
    Map<String, Object> analyzeExerciseHabit(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 用药依从性分析
     */
    Map<String, Object> analyzeMedicationAdherence(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 健康风险评估
     */
    Map<String, Object> assessHealthRisk(Long userId);

    /**
     * 趋势预测分析
     */
    Map<String, Object> predictHealthTrend(Long userId, int forecastDays);

    /**
     * 综合健康评分
     */
    Integer calculateOverallHealthScore(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 生成个性化建议
     */
    Map<String, String> generatePersonalizedAdvice(Long userId);

    /**
     * 对比分析（与历史数据对比）
     */
    Map<String, Object> compareWithHistory(Long userId, LocalDate currentPeriodStart, LocalDate historicalPeriodStart);
}
