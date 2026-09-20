package com.atguigu.consult.service;


import com.atguigu.consult.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);

    /**
     * 统计指定科室/日期/时段（可选医生）下已被占用的号源数量
     */
    long countOccupiedSlots(String department, String date, String time, String doctorName);
}
