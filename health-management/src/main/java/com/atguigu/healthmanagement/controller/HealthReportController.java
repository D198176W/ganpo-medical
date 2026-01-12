package com.atguigu.healthmanagement.controller;

import com.atguigu.healthmanagement.entity.HealthReport;
import com.atguigu.healthmanagement.service.IHealthReportService;
import com.atguigu.healthmanagement.util.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/health-reports")
@RequiredArgsConstructor
public class HealthReportController {

    private final IHealthReportService healthReportService;

    /**
     * 获取用户健康报告列表
     */
    @GetMapping
    public Result<List<HealthReport>> getReportsByUser(@RequestParam Long userId) {
        try {
            List<HealthReport> reports = healthReportService.getReportsByUserId(userId);
            return Result.success(reports);
        } catch (Exception e) {
            log.error("获取健康报告列表失败", e);
            return Result.error("获取健康报告列表失败");
        }
    }

    /**
     * 根据ID获取健康报告详情
     */
    @GetMapping("/{id}")
    public Result<HealthReport> getReportById(@PathVariable Long id) {
        try {
            HealthReport report = healthReportService.getById(id);
            if (report != null) {
                return Result.success(report);
            } else {
                return Result.notFound("健康报告不存在");
            }
        } catch (Exception e) {
            log.error("获取健康报告详情失败", e);
            return Result.error("获取健康报告详情失败");
        }
    }

    /**
     * 创建健康报告
     */
    @PostMapping
    public Result<HealthReport> createReport(@RequestBody HealthReport report) {
        try {
            boolean success = healthReportService.save(report);
            if (success) {
                log.info("创建健康报告成功: {}", report.getId());
                return Result.success(report, "创建成功");
            } else {
                return Result.error("创建健康报告失败");
            }
        } catch (Exception e) {
            log.error("创建健康报告失败", e);
            return Result.error("创建健康报告失败");
        }
    }

    /**
     * 更新健康报告
     */
    @PutMapping("/{id}")
    public Result<HealthReport> updateReport(@PathVariable Long id, @RequestBody HealthReport report) {
        try {
            report.setId(id);
            boolean success = healthReportService.updateById(report);
            if (success) {
                log.info("更新健康报告成功: {}", id);
                return Result.success(report, "更新成功");
            } else {
                return Result.error("更新健康报告失败");
            }
        } catch (Exception e) {
            log.error("更新健康报告失败", e);
            return Result.error("更新健康报告失败");
        }
    }

    /**
     * 删除健康报告
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteReport(@PathVariable Long id) {
        try {
            boolean success = healthReportService.removeById(id);
            if (success) {
                log.info("删除健康报告成功: {}", id);
                return Result.success(true, "删除成功");
            } else {
                return Result.error("删除健康报告失败");
            }
        } catch (Exception e) {
            log.error("删除健康报告失败", e);
            return Result.error("删除健康报告失败");
        }
    }

    /**
     * 生成健康报告
     */
    @PostMapping("/generate")
    public Result<HealthReport> generateReport(@RequestParam Long userId,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            HealthReport report = healthReportService.generateHealthReport(userId, startDate, endDate);
            return Result.success(report, "健康报告生成成功");
        } catch (Exception e) {
            log.error("生成健康报告失败", e);
            return Result.error("生成健康报告失败");
        }
    }
}
