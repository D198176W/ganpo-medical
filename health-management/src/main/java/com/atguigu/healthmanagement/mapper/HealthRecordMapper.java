package com.atguigu.healthmanagement.mapper;

import com.atguigu.healthmanagement.entity.HealthRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface HealthRecordMapper extends BaseMapper<HealthRecord> {

    @Select("SELECT * FROM health_records WHERE user_id = #{userId} AND record_date = #{recordDate}")
    HealthRecord selectByUserIdAndDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);

    @Select("SELECT * FROM health_records WHERE user_id = #{userId} AND YEAR(record_date) = #{year} AND MONTH(record_date) = #{month}")
    List<HealthRecord> selectByUserIdAndMonth(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    @Select("SELECT record_date, COUNT(*) as record_count FROM health_records WHERE user_id = #{userId} GROUP BY record_date")
    List<Map<String, Object>> selectCalendarData(@Param("userId") Long userId);

    Page<HealthRecord> selectPageByUserId(Page<HealthRecord> page, @Param("userId") Long userId);


    Object countRecordsInPeriod(Long userId, LocalDate startDate, LocalDate endDate);

    int countContinuousRecordDays(Long userId);

    HealthRecord selectLatestRecord(Long userId);

    Object getAverageSleepScore(Long userId, LocalDate startDate, LocalDate endDate);

    Object countExerciseDays(Long userId, LocalDate startDate, LocalDate endDate);

    List<HealthRecord> selectByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
}
