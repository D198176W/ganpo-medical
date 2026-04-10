package com.atguigu.healthmanagement.dto;

import lombok.Data;

@Data
public class UserStats {
    private Long totalRecords;
    private Long totalReports;
    private Integer continuousDays;
    private Double healthScore;
    private Integer exerciseDays;
    private Double avgSleepScore;
    private Integer currentStreak;

    public UserStats() {}

    public UserStats(Long totalRecords, Long totalReports, Integer continuousDays, Double healthScore) {
        this.totalRecords = totalRecords;
        this.totalReports = totalReports;
        this.continuousDays = continuousDays;
        this.healthScore = healthScore;
    }
}
