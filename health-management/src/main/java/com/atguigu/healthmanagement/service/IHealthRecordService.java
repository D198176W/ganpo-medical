package com.atguigu.healthmanagement.service;

import com.atguigu.healthmanagement.entity.HealthRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

//public interface IHealthRecordService extends IService<HealthRecord> {
//    HealthRecord getByUserIdAndDate(Long userId, LocalDate date);
//    List<HealthRecord> getMonthlyRecords(Long userId, int year, int month);
//    Map<LocalDate, Boolean> getCalendarData(Long userId);
//    Page<HealthRecord> getPageByUserId(Page<HealthRecord> page, Long userId);
//    boolean saveOrUpdateRecord(HealthRecord healthRecord);
//}
/**
 * 健康记录服务接口
 */
public interface IHealthRecordService extends IService<HealthRecord> {

    /**
     * 根据用户ID和日期查询健康记录
     */
    HealthRecord getByUserIdAndDate(Long userId, LocalDate recordDate);

    /**
     * 获取用户某月的健康记录
     */
    List<HealthRecord> getMonthlyRecords(Long userId, int year, int month);

    /**
     * 获取用户日历数据（标记有记录的日子）
     */
    Map<LocalDate, Boolean> getCalendarData(Long userId, int year, int month);

    /**
     * 保存或更新健康记录
     */
    boolean saveOrUpdateRecord(HealthRecord healthRecord);

    /**
     * 获取用户连续记录天数
     */
    int getContinuousRecordDays(Long userId);

    /**
     * 获取用户健康记录统计
     */
    Map<String, Object> getRecordStats(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 检查某天是否有记录
     */
    boolean hasRecordOnDate(Long userId, LocalDate date);

    /**
     * 删除某天的记录
     */
    boolean deleteRecordByDate(Long userId, LocalDate date);

    /**
     * 获取用户最近一次记录
     */
    HealthRecord getLatestRecord(Long userId);

    Page<HealthRecord> getPageByUserId(Page<HealthRecord> page, Long userId);

    Map<String, Object > getTodayHealthData(Long userId);
}