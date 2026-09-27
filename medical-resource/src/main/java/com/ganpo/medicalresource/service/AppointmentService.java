package com.ganpo.medicalresource.service;


import com.ganpo.medicalresource.entity.Appointment;

public interface AppointmentService {
    Appointment create(Appointment appointment);
    Appointment getById(Long id);
}
