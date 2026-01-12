//package com.atguigu.healthmanagement.service.impl;
//
//import com.atguigu.healthmanagement.entity.HealthRecord;
//import com.atguigu.healthmanagement.entity.HealthReport;
//import com.atguigu.healthmanagement.mapper.HealthRecordMapper;
//import com.atguigu.healthmanagement.mapper.HealthReportMapper;
//import com.atguigu.healthmanagement.service.IHealthReportService;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.*;
////
////@Service
////public class HealthReportServiceImpl extends ServiceImpl<HealthReportMapper, HealthReport>
////        implements IHealthReportService {
////
////    @Override
////    public HealthReport generateHealthReport(Long userId, LocalDate startDate, LocalDate endDate) {
////        return null;
////    }
////
////    @Override
////    public List<HealthReport> getReportsByUserId(Long userId) {
////        return null;
////    }
////
////    @Override
////    public HealthReport getLatestReport(Long userId) {
////        return null;
////    }
////
////    @Override
////    public List<HealthReport> getReportsByType(Long userId, String reportType) {
////        return null;
////    }
////
////    @Override
////    public Map<String, Object> getHealthScoreTrend(Long userId, int months) {
////        return null;
////    }
////
////    @Override
////    public Map<String, Object> analyzeHealthImprovement(Long userId, LocalDate startDate, LocalDate endDate) {
////        return null;
////    }
////
////    @Override
////    public Map<String, Object> getReportStatistics(Long userId) {
////        return null;
////    }
////
////    @Override
////    public boolean publishReport(Long reportId) {
////        return false;
////    }
////
////    @Override
////    public List<Map<String, Object>> getPendingReports() {
////        return null;
////    }
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public  class HealthReportServiceImpl extends ServiceImpl<HealthReportMapper, HealthReport>
//            implements IHealthReportService {
//
//        private final HealthReportMapper healthReportMapper;
//        private final HealthRecordMapper healthRecordMapper;
//
//        @Override
//        public HealthReport generateHealthReport(Long userId, LocalDate startDate, LocalDate endDate) {
//            try {
//                log.info("开始生成健康报告: userId={}, startDate={}, endDate={}", userId, startDate, endDate);
//
//                // 1. 获取时间段内的健康记录
//                List<HealthRecord> records = healthRecordMapper.selectByUserIdAndDateRange(userId, startDate, endDate);
//                if (records.isEmpty()) {
//                    log.warn("该时间段内没有健康记录数据");
//                    return null;
//                }
//
//                // 2. 计算各项指标
//                Map<String, Object> analysis = analyzeHealthData(records);
//
//                // 3. 创建健康报告
//                HealthReport report = new HealthReport();
//                report.setUserId(userId);
//                report.setTitle(String.format("%s至%s健康报告", startDate, endDate));
//                report.setReportType(HealthReport.ReportType.CUSTOM);
//                report.setStartDate(startDate);
//                report.setEndDate(endDate);
//                report.setGeneratedAt(java.time.LocalDateTime.now());
//                report.setReportStatus(HealthReport.ReportStatus.GENERATED);
//
//                // 4. 设置评分数据
//                report.setOverallScore((Integer) analysis.get("overallScore"));
//                report.setSleepScore((Integer) analysis.get("sleepScore"));
//                report.setExerciseScore((Integer) analysis.get("exerciseScore"));
//                report.setDietScore((Integer) analysis.get("dietScore"));
//                report.setMentalScore((Integer) analysis.get("mentalScore"));
//                report.calculateHealthGrade();
//
//                // 5. 设置详细内容
//                report.setSummaryContent(generateSummaryContent(analysis));
//                report.setDetailContent(generateDetailContent(analysis));
//                report.setDoctorAdvice(generateAdvice(analysis));
//
//                // 6. 保存报告
//                boolean saved = save(report);
//                if (saved) {
//                    log.info("健康报告生成成功: reportId={}", report.getId());
//                    return report;
//                } else {
//                    log.error("保存健康报告失败");
//                    return null;
//                }
//
//            } catch (Exception e) {
//                log.error("生成健康报告失败", e);
//                return null;
//            }
//        }
//
//        @Override
//        public List<HealthReport> getReportsByUserId(Long userId) {
//            try {
//                QueryWrapper<HealthReport> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("user_id", userId)
//                        .orderByDesc("generated_at");
//                return list(queryWrapper);
//            } catch (Exception e) {
//                log.error("获取用户健康报告列表失败: userId={}", userId, e);
//                return Collections.emptyList();
//            }
//        }
//
//        @Override
//        public HealthReport getLatestReport(Long userId) {
//            try {
//                QueryWrapper<HealthReport> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("user_id", userId)
//                        .orderByDesc("generated_at")
//                        .last("LIMIT 1");
//                return getOne(queryWrapper);
//            } catch (Exception e) {
//                log.error("获取最新健康报告失败: userId={}", userId, e);
//                return null;
//            }
//        }
//
//        @Override
//        public List<HealthReport> getReportsByType(Long userId, String reportType) {
//            try {
//                QueryWrapper<HealthReport> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("user_id", userId)
//                        .eq("report_type", reportType)
//                        .orderByDesc("generated_at");
//                return list(queryWrapper);
//            } catch (Exception e) {
//                log.error("按类型获取健康报告失败: userId={}, type={}", userId, reportType, e);
//                return Collections.emptyList();
//            }
//        }
//
//        @Override
//        public Map<String, Object> getHealthScoreTrend(Long userId, int months) {
//            try {
//                LocalDate endDate = LocalDate.now();
//                LocalDate startDate = endDate.minusMonths(months);
//
//                List<HealthReport> reports = healthReportMapper.selectByUserIdAndDateRange(
//                        userId, startDate, endDate);
//
//                Map<String, Object> trend = new HashMap<>();
//                List<Map<String, Object>> scoreData = new ArrayList<>();
//
//                for (HealthReport report : reports) {
//                    Map<String, Object> dataPoint = new HashMap<>();
//                    dataPoint.put("date", report.getStartDate().toString());
//                    dataPoint.put("overallScore", report.getOverallScore());
//                    dataPoint.put("sleepScore", report.getSleepScore());
//                    dataPoint.put("exerciseScore", report.getExerciseScore());
//                    dataPoint.put("dietScore", report.getDietScore());
//                    dataPoint.put("mentalScore", report.getMentalScore());
//                    scoreData.add(dataPoint);
//                }
//
//                trend.put("scoreData", scoreData);
//                trend.put("averageScore", calculateAverageScore(reports));
//                trend.put("trendDirection", analyzeTrendDirection(scoreData));
//
//                return trend;
//            } catch (Exception e) {
//                log.error("获取健康评分趋势失败: userId={}, months={}", userId, months, e);
//                return Collections.emptyMap();
//            }
//        }
//
//        @Override
//        public Map<String, Object> analyzeHealthImprovement(Long userId, LocalDate startDate, LocalDate endDate) {
//            try {
//                // 获取当前时间段报告
//                HealthReport currentReport = generateHealthReport(userId, startDate, endDate);
//
//                // 获取历史同期报告（例如：上个月、上个季度）
//                LocalDate historicalStart = startDate.minusMonths(1);
//                LocalDate historicalEnd = endDate.minusMonths(1);
//                List<HealthReport> historicalReports = healthReportMapper.selectByUserIdAndDateRange(
//                        userId, historicalStart, historicalEnd);
//
//                Map<String, Object> improvement = new HashMap<>();
//
//                if (currentReport != null && !historicalReports.isEmpty()) {
//                    HealthReport historicalReport = historicalReports.get(0);
//
//                    improvement.put("currentScore", currentReport.getOverallScore());
//                    improvement.put("historicalScore", historicalReport.getOverallScore());
//                    improvement.put("scoreChange", currentReport.getOverallScore() - historicalReport.getOverallScore());
//                    improvement.put("improvementAreas", identifyImprovementAreas(currentReport, historicalReport));
//                    improvement.put("regressionAreas", identifyRegressionAreas(currentReport, historicalReport));
//                    improvement.put("recommendations", generateImprovementRecommendations(improvement));
//                }
//
//                return improvement;
//            } catch (Exception e) {
//                log.error("分析健康改善情况失败", e);
//                return Collections.emptyMap();
//            }
//        }
//
//        @Override
//        public Map<String, Object> getReportStatistics(Long userId) {
//            try {
//                List<HealthReport> allReports = getReportsByUserId(userId);
//
//                Map<String, Object> stats = new HashMap<>();
//                stats.put("totalReports", allReports.size());
//                stats.put("averageScore", calculateAverageScore(allReports));
//                stats.put("bestScore", findBestScore(allReports));
//                stats.put("worstScore", findWorstScore(allReports));
//                stats.put("reportTypes", groupByReportType(allReports));
//                stats.put("monthlyTrend", calculateMonthlyTrend(allReports));
//
//                return stats;
//            } catch (Exception e) {
//                log.error("获取报告统计信息失败: userId={}", userId, e);
//                return Collections.emptyMap();
//            }
//        }
//
//        @Override
//        public boolean publishReport(Long reportId) {
//            try {
//                HealthReport report = getById(reportId);
//                if (report == null) {
//                    log.warn("报告不存在: reportId={}", reportId);
//                    return false;
//                }
//
//                report.setReportStatus(HealthReport.ReportStatus.PUBLISHED);
//                boolean updated = updateById(report);
//
//                if (updated) {
//                    log.info("报告发布成功: reportId={}", reportId);
//                    // 这里可以添加通知逻辑，比如发送邮件或消息
//                }
//
//                return updated;
//            } catch (Exception e) {
//                log.error("发布报告失败: reportId={}", reportId, e);
//                return false;
//            }
//        }
//
//        @Override
//        public List<Map<String, Object>> getPendingReports() {
//            try {
//                QueryWrapper<HealthReport> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("report_status", HealthReport.ReportStatus.GENERATED.name())
//                        .or().eq("report_status", HealthReport.ReportStatus.DRAFT.name())
//                        .orderByAsc("generated_at");
//
//                List<HealthReport> pendingReports = list(queryWrapper);
//                List<Map<String, Object>> result = new ArrayList<>();
//
//                for (HealthReport report : pendingReports) {
//                    Map<String, Object> reportInfo = new HashMap<>();
//                    reportInfo.put("reportId", report.getId());
//                    reportInfo.put("title", report.getTitle());
//                    reportInfo.put("userId", report.getUserId());
//                    reportInfo.put("generatedAt", report.getGeneratedAt());
//                    reportInfo.put("status", report.getReportStatus());
//                    reportInfo.put("overallScore", report.getOverallScore());
//                    result.add(reportInfo);
//                }
//
//                return result;
//            } catch (Exception e) {
//                log.error("获取待处理报告列表失败", e);
//                return Collections.emptyList();
//            }
//        }
//
//        // ========== 私有辅助方法 ==========
//
//        /**
//         * 分析健康数据
//         */
//        private Map<String, Object> analyzeHealthData(List<HealthRecord> records) {
//            Map<String, Object> analysis = new HashMap<>();
//
//            // 计算平均睡眠评分
//            double avgSleepScore = records.stream()
//                    .filter(r -> r.getSleepScore() != null)
//                    .mapToInt(HealthRecord::getSleepScore)
//                    .average()
//                    .orElse(0);
//
//            // 计算运动天数比例
//            long exerciseDays = records.stream()
//                    .filter(r -> r.getExerciseDuration() != null && r.getExerciseDuration() > 0)
//                    .count();
//            double exerciseRatio = (double) exerciseDays / records.size();
//
//            // 计算饮食评分（基于饮食记录）
//            double avgDietScore = records.stream()
//                    .filter(r -> r.getDietScore() != null)
//                    .mapToInt(HealthRecord::getDietScore)
//                    .average()
//                    .orElse(70);
//
//            // 计算心理状态评分
//            double avgMentalScore = records.stream()
//                    .filter(r -> r.getMoodScore() != null)
//                    .mapToInt(HealthRecord::getMoodScore)
//                    .average()
//                    .orElse(75);
//
//            // 计算总体评分（加权平均）
//            double overallScore = (avgSleepScore * 0.3) + (exerciseRatio * 100 * 0.25) +
//                    (avgDietScore * 0.25) + (avgMentalScore * 0.2);
//
//            analysis.put("sleepScore", (int) Math.round(avgSleepScore));
//            analysis.put("exerciseScore", (int) Math.round(exerciseRatio * 100));
//            analysis.put("dietScore", (int) Math.round(avgDietScore));
//            analysis.put("mentalScore", (int) Math.round(avgMentalScore));
//            analysis.put("overallScore", (int) Math.round(overallScore));
//            analysis.put("recordCount", records.size());
//            analysis.put("analysisDate", LocalDate.now());
//
//            return analysis;
//        }
//
//        /**
//         * 生成摘要内容
//         */
//        private String generateSummaryContent(Map<String, Object> analysis) {
//            int overallScore = (Integer) analysis.get("overallScore");
//            int recordCount = (Integer) analysis.get("recordCount");
//
//            StringBuilder summary = new StringBuilder();
//            summary.append(String.format("基于%d条健康记录的分析显示，您的总体健康评分为%d分。", recordCount, overallScore));
//
//            if (overallScore >= 90) {
//                summary.append("您的健康状况非常优秀！");
//            } else if (overallScore >= 80) {
//                summary.append("您的健康状况良好，继续保持！");
//            } else if (overallScore >= 70) {
//                summary.append("您的健康状况一般，有提升空间。");
//            } else {
//                summary.append("请关注您的健康状况，建议咨询医生。");
//            }
//
//            return summary.toString();
//        }
//
//        /**
//         * 生成详细内容
//         */
//        private String generateDetailContent(Map<String, Object> analysis) {
//            Map<String, Object> detail = new HashMap<>();
//            detail.put("analysis", analysis);
//            detail.put("generatedAt", java.time.LocalDateTime.now().toString());
//
//            // 转换为JSON字符串（实际项目中可以使用Jackson或Gson）
//            return detail.toString();
//        }
//
//        /**
//         * 生成建议
//         */
//        private String generateAdvice(Map<String, Object> analysis) {
//            int sleepScore = (Integer) analysis.get("sleepScore");
//            int exerciseScore = (Integer) analysis.get("exerciseScore");
//            int dietScore = (Integer) analysis.get("dietScore");
//
//            List<String> adviceList = new ArrayList<>();
//
//            if (sleepScore < 80) {
//                adviceList.add("建议保持规律作息，保证每晚7-8小时睡眠。");
//            }
//
//            if (exerciseScore < 80) {
//                adviceList.add("建议每周进行3-5次中等强度运动，每次30分钟以上。");
//            }
//
//            if (dietScore < 80) {
//                adviceList.add("建议均衡饮食，多吃蔬菜水果，控制油盐摄入。");
//            }
//
//            if (adviceList.isEmpty()) {
//                return "您的各项指标都很健康，请继续保持良好的生活习惯！";
//            }
//
//            return String.join(" ", adviceList);
//        }
//
//        /**
//         * 计算平均分数
//         */
//        private double calculateAverageScore(List<HealthReport> reports) {
//            return reports.stream()
//                    .filter(r -> r.getOverallScore() != null)
//                    .mapToInt(HealthReport::getOverallScore)
//                    .average()
//                    .orElse(0);
//        }
//
//        /**
//         * 分析趋势方向
//         */
//        private String analyzeTrendDirection(List<Map<String, Object>> scoreData) {
//            if (scoreData.size() < 2) {
//                return "stable";
//            }
//
//            int firstScore = (Integer) scoreData.get(0).get("overallScore");
//            int lastScore = (Integer) scoreData.get(scoreData.size() - 1).get("overallScore");
//
//            if (lastScore > firstScore + 5) {
//                return "improving";
//            } else if (lastScore < firstScore - 5) {
//                return "declining";
//            } else {
//                return "stable";
//            }
//        }
//
//        /**
//         * 识别改善领域
//         */
//        private List<String> identifyImprovementAreas(HealthReport current, HealthReport historical) {
//            List<String> improvements = new ArrayList<>();
//
//            if (current.getSleepScore() > historical.getSleepScore() + 5) {
//                improvements.add("睡眠质量有所改善");
//            }
//            if (current.getExerciseScore() > historical.getExerciseScore() + 5) {
//                improvements.add("运动习惯有所改善");
//            }
//            if (current.getDietScore() > historical.getDietScore() + 5) {
//                improvements.add("饮食习惯有所改善");
//            }
//
//            return improvements;
//        }
//
//        /**
//         * 识别退步领域
//         */
//        private List<String> identifyRegressionAreas(HealthReport current, HealthReport historical) {
//            List<String> regressions = new ArrayList<>();
//
//            if (current.getSleepScore() < historical.getSleepScore() - 5) {
//                regressions.add("睡眠质量有所下降");
//            }
//            if (current.getExerciseScore() < historical.getExerciseScore() - 5) {
//                regressions.add("运动频率有所减少");
//            }
//            if (current.getDietScore() < historical.getDietScore() - 5) {
//                regressions.add("饮食均衡性有待提高");
//            }
//
//            return regressions;
//        }
//
//        /**
//         * 生成改善建议
//         */
//        private List<String> generateImprovementRecommendations(Map<String, Object> improvement) {
//            List<String> recommendations = new ArrayList<>();
//            int scoreChange = (Integer) improvement.get("scoreChange");
//
//            if (scoreChange > 0) {
//                recommendations.add("继续保持当前的良好习惯");
//            } else if (scoreChange < 0) {
//                recommendations.add("建议关注健康指标的下降趋势");
//            }
//
//            @SuppressWarnings("unchecked")
//            List<String> improvementAreas = (List<String>) improvement.get("improvementAreas");
//            if (improvementAreas != null && !improvementAreas.isEmpty()) {
//                recommendations.add("在以下方面继续保持:" + String.join(",", improvementAreas));
//            }
//
//            @SuppressWarnings("unchecked")
//            List<String> regressionAreas = (List<String>) improvement.get("regressionAreas");
//            if (regressionAreas != null && !regressionAreas.isEmpty()) {
//                recommendations.add("需要关注以下方面:" + String.join(",", regressionAreas));
//            }
//
//            return recommendations;
//        }
//
//        /**
//         * 查找最佳分数
//         */
//        private Integer findBestScore(List<HealthReport> reports) {
//            return reports.stream()
//                    .filter(r -> r.getOverallScore() != null)
//                    .mapToInt(HealthReport::getOverallScore)
//                    .max()
//                    .orElse(0);
//        }
//
//        /**
//         * 查找最差分数
//         */
//        private Integer findWorstScore(List<HealthReport> reports) {
//            return reports.stream()
//                    .filter(r -> r.getOverallScore() != null)
//                    .mapToInt(HealthReport::getOverallScore)
//                    .min()
//                    .orElse(0);
//        }
//
//        /**
//         * 按报告类型分组
//         */
//        private Map<String, Long> groupByReportType(List<HealthReport> reports) {
//            Map<String, Long> typeCount = new HashMap<>();
//            for (HealthReport report : reports) {
//                String type = report.getReportType().name();
//                typeCount.put(type, typeCount.getOrDefault(type, 0L) + 1);
//            }
//            return typeCount;
//        }
//
//        /**
//         * 计算月度趋势
//         */
//        private Map<String, Object> calculateMonthlyTrend(List<HealthReport> reports) {
//            Map<String, Object> monthlyTrend = new HashMap<>();
//            // 实现月度趋势计算逻辑
//            return monthlyTrend;
//        }
//    }
//}


package com.atguigu.healthmanagement.service.impl;

import com.atguigu.healthmanagement.entity.HealthRecord;
import com.atguigu.healthmanagement.entity.HealthReport;
import com.atguigu.healthmanagement.mapper.HealthRecordMapper;
import com.atguigu.healthmanagement.mapper.HealthReportMapper;
import com.atguigu.healthmanagement.service.IHealthReportService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthReportServiceImpl extends ServiceImpl<HealthReportMapper, HealthReport>
        implements IHealthReportService {

    private final HealthReportMapper healthReportMapper;
    private final HealthRecordMapper healthRecordMapper;

    @Override
    public HealthReport generateHealthReport(Long userId, LocalDate startDate, LocalDate endDate) {
        try {
//            log.info("开始生成健康报告: userId={}, startDate={}, endDate={}", userId, startDate, endDate);

            // 1. 获取时间段内的健康记录
            List<HealthRecord> records = healthRecordMapper.selectByUserIdAndDateRange(userId, startDate, endDate);
            if (records.isEmpty()) {
                log.warn("该时间段内没有健康记录数据");
                return null;
            }

            // 2. 计算各项指标
            Map<String, Object> analysis = analyzeHealthData(records);

            // 3. 创建健康报告
            HealthReport report = new HealthReport();
//            report.setUserId(userId);
//            report.setTitle(String.format("%s至%s健康报告", startDate, endDate));
//            report.setReportType(HealthReport.ReportType.CUSTOM);
//            report.setStartDate(startDate);
//            report.setEndDate(endDate);
//            report.setGeneratedAt(java.time.LocalDateTime.now());
//            report.setReportStatus(HealthReport.ReportStatus.GENERATED);

            // 4. 设置评分数据
//            report.setOverallScore((Integer) analysis.get("overallScore"));
//            report.setSleepScore((Integer) analysis.get("sleepScore"));
//            report.setExerciseScore((Integer) analysis.get("exerciseScore"));
//            report.setDietScore((Integer) analysis.get("dietScore"));
//            report.setMentalScore((Integer) analysis.get("mentalScore"));
            report.calculateHealthGrade();

            // 5. 设置详细内容
//            report.setSummaryContent(generateSummaryContent(analysis));
//            report.setDetailContent(generateDetailContent(analysis));
//            report.setDoctorAdvice(generateAdvice(analysis));

            // 6. 保存报告
            boolean saved = save(report);
            if (saved) {
//                log.info("健康报告生成成功: reportId={}", report.getId());
                return report;
            } else {
//                log.error("保存健康报告失败");
                return null;
            }

        } catch (Exception e) {
            log.error("生成健康报告失败", e);
            return null;
        }
    }

    @Override
    public List<HealthReport> getReportsByUserId(Long userId) {
        try {
            QueryWrapper<HealthReport> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                    .orderByDesc("generated_at");
            return list(queryWrapper);
        } catch (Exception e) {
//            log.error("获取用户健康报告列表失败: userId={}", userId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public HealthReport getLatestReport(Long userId) {
        try {
            QueryWrapper<HealthReport> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                    .orderByDesc("generated_at")
                    .last("LIMIT 1");
            return getOne(queryWrapper);
        } catch (Exception e) {
//            log.error("获取最新健康报告失败: userId={}", userId, e);
            return null;
        }
    }

    @Override
    public List<HealthReport> getReportsByType(Long userId, String reportType) {
        try {
            QueryWrapper<HealthReport> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                    .eq("report_type", reportType)
                    .orderByDesc("generated_at");
            return list(queryWrapper);
        } catch (Exception e) {
//            log.error("按类型获取健康报告失败: userId={}, type={}", userId, reportType, e);
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> getHealthScoreTrend(Long userId, int months) {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(months);

            List<HealthReport> reports = healthReportMapper.selectByUserIdAndDateRange(
                    userId, startDate, endDate);

            Map<String, Object> trend = new HashMap<>();
            List<Map<String, Object>> scoreData = new ArrayList<>();

            for (HealthReport report : reports) {
                Map<String, Object> dataPoint = new HashMap<>();
//                dataPoint.put("date", report.getStartDate().toString());
//                dataPoint.put("overallScore", report.getOverallScore());
//                dataPoint.put("sleepScore", report.getSleepScore());
//                dataPoint.put("exerciseScore", report.getExerciseScore());
//                dataPoint.put("dietScore", report.getDietScore());
//                dataPoint.put("mentalScore", report.getMentalScore());
                scoreData.add(dataPoint);
            }

            trend.put("scoreData", scoreData);
            trend.put("averageScore", calculateAverageScore(reports));
            trend.put("trendDirection", analyzeTrendDirection(scoreData));

            return trend;
        } catch (Exception e) {
            log.error("获取健康评分趋势失败: userId={}, months={}", userId, months, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Object> analyzeHealthImprovement(Long userId, LocalDate startDate, LocalDate endDate) {
        try {
            // 获取当前时间段报告
            HealthReport currentReport = generateHealthReport(userId, startDate, endDate);

            // 获取历史同期报告（例如：上个月、上个季度）
            LocalDate historicalStart = startDate.minusMonths(1);
            LocalDate historicalEnd = endDate.minusMonths(1);
            List<HealthReport> historicalReports = healthReportMapper.selectByUserIdAndDateRange(
                    userId, historicalStart, historicalEnd);

            Map<String, Object> improvement = new HashMap<>();

            if (currentReport != null && !historicalReports.isEmpty()) {
                HealthReport historicalReport = historicalReports.get(0);

//                improvement.put("currentScore", currentReport.getOverallScore());
//                improvement.put("historicalScore", historicalReport.getOverallScore());
//                improvement.put("scoreChange", currentReport.getOverallScore() - historicalReport.getOverallScore());
                improvement.put("improvementAreas", identifyImprovementAreas(currentReport, historicalReport));
                improvement.put("regressionAreas", identifyRegressionAreas(currentReport, historicalReport));
                improvement.put("recommendations", generateImprovementRecommendations(improvement));
            }

            return improvement;
        } catch (Exception e) {
            log.error("分析健康改善情况失败", e);
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Object> getReportStatistics(Long userId) {
        try {
            List<HealthReport> allReports = getReportsByUserId(userId);

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalReports", allReports.size());
            stats.put("averageScore", calculateAverageScore(allReports));
            stats.put("bestScore", findBestScore(allReports));
            stats.put("worstScore", findWorstScore(allReports));
            stats.put("reportTypes", groupByReportType(allReports));
            stats.put("monthlyTrend", calculateMonthlyTrend(allReports));

            return stats;
        } catch (Exception e) {
            log.error("获取报告统计信息失败: userId={}", userId, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public boolean publishReport(Long reportId) {
        try {
            HealthReport report = getById(reportId);
            if (report == null) {
                log.warn("报告不存在: reportId={}", reportId);
                return false;
            }

            report.setReportStatus(HealthReport.ReportStatus.PUBLISHED);
            boolean updated = updateById(report);

            if (updated) {
                log.info("报告发布成功: reportId={}", reportId);
                // 这里可以添加通知逻辑，比如发送邮件或消息
            }

            return updated;
        } catch (Exception e) {
            log.error("发布报告失败: reportId={}", reportId, e);
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getPendingReports() {
        try {
            QueryWrapper<HealthReport> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("report_status", HealthReport.ReportStatus.GENERATED.name())
                    .or().eq("report_status", HealthReport.ReportStatus.DRAFT.name())
                    .orderByAsc("generated_at");

            List<HealthReport> pendingReports = list(queryWrapper);
            List<Map<String, Object>> result = new ArrayList<>();

            for (HealthReport report : pendingReports) {
                Map<String, Object> reportInfo = new HashMap<>();
                reportInfo.put("reportId", report.getId());
                reportInfo.put("title", report.getTitle());
                reportInfo.put("userId", report.getUserId());
//                reportInfo.put("generatedAt", report.getGeneratedAt());
                reportInfo.put("status", report.getReportStatus());
                reportInfo.put("overallScore", report.getOverallScore());
                result.add(reportInfo);
            }

            return result;
        } catch (Exception e) {
            log.error("获取待处理报告列表失败", e);
            return Collections.emptyList();
        }
    }

    // ========== 私有辅助方法 ==========

    /**
     * 分析健康数据
     */
    private Map<String, Object> analyzeHealthData(List<HealthRecord> records) {
        Map<String, Object> analysis = new HashMap<>();

        // 计算平均睡眠评分
        double avgSleepScore = records.stream()
                .filter(r -> r.getSleepScore() != null)
                .mapToInt(HealthRecord::getSleepScore)
                .average()
                .orElse(0);

        // 计算运动天数比例
        long exerciseDays = records.stream()
                .filter(r -> r.getExerciseDuration() != null && r.getExerciseDuration() > 0)
                .count();
        double exerciseRatio = (double) exerciseDays / records.size();

        // 计算饮食评分（基于饮食记录）
        double avgDietScore = records.stream()
                .filter(r -> r.getDietScore() != null)
                .mapToInt(HealthRecord::getDietScore)
                .average()
                .orElse(70);

        // 计算心理状态评分
        double avgMentalScore = records.stream()
                .filter(r -> r.getMoodScore() != null)
                .mapToInt(HealthRecord::getMoodScore)
                .average()
                .orElse(75);

        // 计算总体评分（加权平均）
        double overallScore = (avgSleepScore * 0.3) + (exerciseRatio * 100 * 0.25) +
                (avgDietScore * 0.25) + (avgMentalScore * 0.2);

        analysis.put("sleepScore", (int) Math.round(avgSleepScore));
        analysis.put("exerciseScore", (int) Math.round(exerciseRatio * 100));
        analysis.put("dietScore", (int) Math.round(avgDietScore));
        analysis.put("mentalScore", (int) Math.round(avgMentalScore));
        analysis.put("overallScore", (int) Math.round(overallScore));
        analysis.put("recordCount", records.size());
        analysis.put("analysisDate", LocalDate.now());

        return analysis;
    }

    /**
     * 生成摘要内容
     */
    private String generateSummaryContent(Map<String, Object> analysis) {
        int overallScore = (Integer) analysis.get("overallScore");
        int recordCount = (Integer) analysis.get("recordCount");

        StringBuilder summary = new StringBuilder();
        summary.append(String.format("基于%d条健康记录的分析显示，您的总体健康评分为%d分。", recordCount, overallScore));

        if (overallScore >= 90) {
            summary.append("您的健康状况非常优秀！");
        } else if (overallScore >= 80) {
            summary.append("您的健康状况良好，继续保持！");
        } else if (overallScore >= 70) {
            summary.append("您的健康状况一般，有提升空间。");
        } else {
            summary.append("请关注您的健康状况，建议咨询医生。");
        }

        return summary.toString();
    }

    /**
     * 生成详细内容
     */
    private String generateDetailContent(Map<String, Object> analysis) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("analysis", analysis);
        detail.put("generatedAt", java.time.LocalDateTime.now().toString());

        // 转换为JSON字符串（实际项目中可以使用Jackson或Gson）
        return detail.toString();
    }

    /**
     * 生成建议
     */
    private String generateAdvice(Map<String, Object> analysis) {
        int sleepScore = (Integer) analysis.get("sleepScore");
        int exerciseScore = (Integer) analysis.get("exerciseScore");
        int dietScore = (Integer) analysis.get("dietScore");

        List<String> adviceList = new ArrayList<>();

        if (sleepScore < 80) {
            adviceList.add("建议保持规律作息，保证每晚7-8小时睡眠。");
        }

        if (exerciseScore < 80) {
            adviceList.add("建议每周进行3-5次中等强度运动，每次30分钟以上。");
        }

        if (dietScore < 80) {
            adviceList.add("建议均衡饮食，多吃蔬菜水果，控制油盐摄入。");
        }

        if (adviceList.isEmpty()) {
            return "您的各项指标都很健康，请继续保持良好的生活习惯！";
        }

        return String.join(" ", adviceList);
    }

    /**
     * 计算平均分数
     */
    private double calculateAverageScore(List<HealthReport> reports) {
        return reports.stream()
                .filter(r -> r.getOverallScore() != null)
                .mapToInt(HealthReport::getOverallScore)
                .average()
                .orElse(0);
    }

    /**
     * 分析趋势方向
     */
    private String analyzeTrendDirection(List<Map<String, Object>> scoreData) {
        if (scoreData.size() < 2) {
            return "stable";
        }

        int firstScore = (Integer) scoreData.get(0).get("overallScore");
        int lastScore = (Integer) scoreData.get(scoreData.size() - 1).get("overallScore");

        if (lastScore > firstScore + 5) {
            return "improving";
        } else if (lastScore < firstScore - 5) {
            return "declining";
        } else {
            return "stable";
        }
    }

    /**
     * 识别改善领域
     */
    private List<String> identifyImprovementAreas(HealthReport current, HealthReport historical) {
        List<String> improvements = new ArrayList<>();

        if (current.getSleepScore() > historical.getSleepScore() + 5) {
            improvements.add("睡眠质量有所改善");
        }
        if (current.getExerciseScore() > historical.getExerciseScore() + 5) {
            improvements.add("运动习惯有所改善");
        }
        if (current.getDietScore() > historical.getDietScore() + 5) {
            improvements.add("饮食习惯有所改善");
        }

        return improvements;
    }

    /**
     * 识别退步领域
     */
    private List<String> identifyRegressionAreas(HealthReport current, HealthReport historical) {
        List<String> regressions = new ArrayList<>();

        if (current.getSleepScore() < historical.getSleepScore() - 5) {
            regressions.add("睡眠质量有所下降");
        }
        if (current.getExerciseScore() < historical.getExerciseScore() - 5) {
            regressions.add("运动频率有所减少");
        }
        if (current.getDietScore() < historical.getDietScore() - 5) {
            regressions.add("饮食均衡性有待提高");
        }

        return regressions;
    }

    /**
     * 生成改善建议
     */
    private List<String> generateImprovementRecommendations(Map<String, Object> improvement) {
        List<String> recommendations = new ArrayList<>();
        int scoreChange = (Integer) improvement.get("scoreChange");

        if (scoreChange > 0) {
            recommendations.add("继续保持当前的良好习惯");
        } else if (scoreChange < 0) {
            recommendations.add("建议关注健康指标的下降趋势");
        }

        @SuppressWarnings("unchecked")
        List<String> improvementAreas = (List<String>) improvement.get("improvementAreas");
        if (improvementAreas != null && !improvementAreas.isEmpty()) {
            recommendations.add("在以下方面继续保持:" + String.join(",", improvementAreas));
        }

        @SuppressWarnings("unchecked")
        List<String> regressionAreas = (List<String>) improvement.get("regressionAreas");
        if (regressionAreas != null && !regressionAreas.isEmpty()) {
            recommendations.add("需要关注以下方面:" + String.join(",", regressionAreas));
        }

        return recommendations;
    }

    /**
     * 查找最佳分数
     */
    private Integer findBestScore(List<HealthReport> reports) {
        return reports.stream()
                .filter(r -> r.getOverallScore() != null)
                .mapToInt(HealthReport::getOverallScore)
                .max()
                .orElse(0);
    }

    /**
     * 查找最差分数
     */
    private Integer findWorstScore(List<HealthReport> reports) {
        return reports.stream()
                .filter(r -> r.getOverallScore() != null)
                .mapToInt(HealthReport::getOverallScore)
                .min()
                .orElse(0);
    }

    /**
     * 按报告类型分组
     */
    private Map<String, Long> groupByReportType(List<HealthReport> reports) {
        Map<String, Long> typeCount = new HashMap<>();
        for (HealthReport report : reports) {
            String type = report.getReportType().name();
            typeCount.put(type, typeCount.getOrDefault(type, 0L) + 1);
        }
        return typeCount;
    }

    /**
     * 计算月度趋势
     */
    private Map<String, Object> calculateMonthlyTrend(List<HealthReport> reports) {
        Map<String, Object> monthlyTrend = new HashMap<>();
        // 实现月度趋势计算逻辑
        return monthlyTrend;
    }
}