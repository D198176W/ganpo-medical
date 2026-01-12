package com.atguigu.consult.service;


import com.atguigu.consult.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);
}
