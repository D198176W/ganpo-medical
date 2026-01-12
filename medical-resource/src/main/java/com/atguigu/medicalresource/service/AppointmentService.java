package com.atguigu.medicalresource.service;


import com.atguigu.medicalresource.entity.Appointment;

public interface AppointmentService {
    Appointment create(Appointment appointment);
    Appointment getById(Long id);
}
