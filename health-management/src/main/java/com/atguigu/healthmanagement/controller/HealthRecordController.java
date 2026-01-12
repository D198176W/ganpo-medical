package com.atguigu.healthmanagement.controller;

import com.atguigu.healthmanagement.entity.HealthRecord;
import com.atguigu.healthmanagement.service.IHealthRecordService;
import com.atguigu.healthmanagement.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/health-records")
public class HealthRecordController {

    @Autowired
    private IHealthRecordService healthRecordService;

    @GetMapping("/calendar")
    public Result<Map<LocalDate, Boolean>> getCalendarData(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam Long userId) {
        try {
            Map<LocalDate, Boolean> calendarData = healthRecordService.getCalendarData(userId,year,month);
            return Result.success(calendarData);
        } catch (Exception e) {
            log.error("获取日历数据失败", e);
            return Result.error("获取日历数据失败");
        }
    }

    @GetMapping("/{date}")
    public Result<HealthRecord> getRecordByDate(
            @RequestParam Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            HealthRecord record = healthRecordService.getByUserIdAndDate(userId, date);
            return Result.success(record);
        } catch (Exception e) {
            log.error("查询健康记录失败", e);
            return Result.error("查询健康记录失败");
        }
    }

    @PostMapping
    public Result<Boolean> saveRecord(@RequestBody HealthRecord healthRecord) {
        try {
            boolean success = healthRecordService.saveOrUpdateRecord(healthRecord);
            return success ? Result.success(true, "保存成功") : Result.error("保存失败");
        } catch (Exception e) {
            log.error("保存健康记录失败", e);
            return Result.error("保存失败");
        }
    }

    @GetMapping("/page")
    public Result<Page<HealthRecord>> getRecordPage(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<HealthRecord> page = new Page<>(current, size);
            Page<HealthRecord> resultPage = healthRecordService.getPageByUserId(page, userId);
            return Result.success(resultPage);
        } catch (Exception e) {
            log.error("分页查询失败", e);
            return Result.error("分页查询失败");
        }
    }

    @DeleteMapping("/{date}")
    public Result<Boolean> deleteRecord(
            @RequestParam Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            HealthRecord record = healthRecordService.getByUserIdAndDate(userId, date);
            if (record != null) {
                boolean success = healthRecordService.removeById(record.getId());
                return success ? Result.success(true, "删除成功") : Result.error("删除失败");
            }
            return Result.error("记录不存在");
        } catch (Exception e) {
            log.error("删除记录失败", e);
            return Result.error("删除失败");
        }
    }
}
