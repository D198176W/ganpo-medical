package com.atguigu.healthmanagement.service;

import com.atguigu.healthmanagement.entity.HealthReport;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 健康报告服务接口
 */
public interface IHealthReportService extends IService<HealthReport> {

    /**
     * 生成健康报告
     */
    HealthReport generateHealthReport(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 根据用户ID获取报告列表
     */
    List<HealthReport> getReportsByUserId(Long userId);

    /**
     * 获取用户最新报告
     */
    HealthReport getLatestReport(Long userId);

    /**
     * 根据报告类型查询
     */
    List<HealthReport> getReportsByType(Long userId, String reportType);

    /**
     * 获取健康评分趋势
     */
    Map<String, Object> getHealthScoreTrend(Long userId, int months);

    /**
     * 分析健康改善情况
     */
    Map<String, Object> analyzeHealthImprovement(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取报告统计信息
     */
    Map<String, Object> getReportStatistics(Long userId);

    /**
     * 发布报告
     */
    boolean publishReport(Long reportId);

    /**
     * 获取待生成的报告列表
     */
    List<Map<String, Object>> getPendingReports();
}