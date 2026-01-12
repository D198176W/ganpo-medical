package com.atguigu.healthmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("health_records")
public class HealthRecord {



    public enum SleepType { EARLY, NORMAL, LATE, INSOMNIA }
    public enum ExerciseType { NONE, RUNNING, YOGA, GYM, WALKING, SWIMMING, CYCLING }
    public enum MedicationStatus { ON_TIME, FORGOT, NOT_NEEDED }

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("record_date")
    private LocalDate recordDate;

    @TableField("sleep_score")
    private Integer sleepScore;

    @TableField("diet_score")
    private Integer dietScore;

    @TableField("mood_score")
    private Integer moodScore;

    @TableField("exercise_score")
    private Integer exerciseScore;

    @TableField("sleep_type")
    private SleepType sleepType;

    @TableField("exercise_type")
    private ExerciseType exerciseType;

    @TableField("exercise_duration")
    private Integer exerciseDuration;

    @TableField("medication_status")
    private MedicationStatus medicationStatus;

    @TableField("notes")
    private String notes;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("blood_pressure_high")
    private Integer bloodPressureHigh;

    @TableField("blood_pressure_low")
    private Integer bloodPressureLow;

    @TableField("heart_rate")
    private Integer heartRate;

    @TableField("weight")
    private Double weight;


}
