package com.atguigu.healthmanagement.service.impl;

import com.atguigu.healthmanagement.entity.HealthRecord;
import com.atguigu.healthmanagement.mapper.HealthRecordMapper;
import com.atguigu.healthmanagement.service.IHealthRecordService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Service
//public class HealthRecordServiceImpl extends ServiceImpl<HealthRecordMapper, HealthRecord> implements IHealthRecordService {
//
//    @Override
//    public HealthRecord getByUserIdAndDate(Long userId, LocalDate date) {
//        return baseMapper.selectByUserIdAndDate(userId, date);
//    }
//
//    @Override
//    public List<HealthRecord> getMonthlyRecords(Long userId, int year, int month) {
//        return baseMapper.selectByUserIdAndMonth(userId, year, month);
//    }
//
//    @Override
//    public Map<LocalDate, Boolean> getCalendarData(Long userId) {
//        List<Map<String, Object>> rawData = baseMapper.selectCalendarData(userId);
//        return rawData.stream()
//                .collect(Collectors.toMap(
//                        map -> (LocalDate) map.get("record_date"),
//                        map -> true
//                ));
//    }
//
//    @Override
//    public Page<HealthRecord> getPageByUserId(Page<HealthRecord> page, Long userId) {
//        return baseMapper.selectPageByUserId(page, userId);
//    }
//
//    @Override
//    public boolean saveOrUpdateRecord(HealthRecord healthRecord) {
//        HealthRecord existingRecord = getByUserIdAndDate(healthRecord.getUserId(), healthRecord.getRecordDate());
//
//        if (existingRecord != null) {
//            healthRecord.setId(existingRecord.getId());
//            return updateById(healthRecord);
//        } else {
//            return save(healthRecord);
//        }
//    }
//}

@Service
@RequiredArgsConstructor
public class HealthRecordServiceImpl extends ServiceImpl<HealthRecordMapper, HealthRecord>
        implements IHealthRecordService {

    private final HealthRecordMapper healthRecordMapper;

    @Override
    public HealthRecord getByUserIdAndDate(Long userId, LocalDate recordDate) {
        return healthRecordMapper.selectByUserIdAndDate(userId, recordDate);
    }

    @Override
    public List<HealthRecord> getMonthlyRecords(Long userId, int year, int month) {
        return healthRecordMapper.selectByUserIdAndMonth(userId, year, month);
    }

    @Override
    public Map<LocalDate, Boolean> getCalendarData(Long userId, int year, int month) {
        List<HealthRecord> records = getMonthlyRecords(userId, year, month);
        Map<LocalDate, Boolean> calendarData = new HashMap<>();
//        records.forEach(record -> calendarData.put(record.getRecordDate(), true));
        return calendarData;
    }

    @Override
    public boolean saveOrUpdateRecord(HealthRecord healthRecord) {
        HealthRecord existing = getByUserIdAndDate(healthRecord.getUserId(), healthRecord.getRecordDate());
        if (existing != null) {
//            healthRecord.setId(existing.getId());
        }
        return saveOrUpdate(healthRecord);
    }

    @Override
    public int getContinuousRecordDays(Long userId) {
        // 实现连续记录天数逻辑
        return healthRecordMapper.countContinuousRecordDays(userId);
    }

    @Override
    public Map<String, Object> getRecordStats(Long userId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", healthRecordMapper.countRecordsInPeriod(userId, startDate, endDate));
        stats.put("averageSleepScore", healthRecordMapper.getAverageSleepScore(userId, startDate, endDate));
        stats.put("exerciseDays", healthRecordMapper.countExerciseDays(userId, startDate, endDate));
        return stats;
    }

    @Override
    public boolean hasRecordOnDate(Long userId, LocalDate date) {
        return getByUserIdAndDate(userId, date) != null;
    }

    @Override
    public boolean deleteRecordByDate(Long userId, LocalDate date) {
        HealthRecord record = getByUserIdAndDate(userId, date);
        if (record != null) {
//            return removeById(record.getId());
        }
        return false;
    }

    @Override
    public HealthRecord getLatestRecord(Long userId) {
        return healthRecordMapper.selectLatestRecord(userId);
    }

    @Override
    public Page<HealthRecord> getPageByUserId(Page<HealthRecord> page, Long userId) {
        return null;
    }

    @Override
    public Map<String, Object> getTodayHealthData(Long userId) {
        return null;
    }


}