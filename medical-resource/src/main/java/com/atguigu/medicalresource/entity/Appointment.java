package com.atguigu.medicalresource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
