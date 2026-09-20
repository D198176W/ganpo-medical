package com.atguigu.medicalresource.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;


@Data
@TableName("appointments")
public class Appointment {
    private Long id;
    private Long userId;
    private Long hospitalId;
    private String departmentId;
    private String doctorId;
    private LocalDate date;
    private String timeslot;
    private String status;
    private String ticketNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private LocalDateTime updatedAt;
}
