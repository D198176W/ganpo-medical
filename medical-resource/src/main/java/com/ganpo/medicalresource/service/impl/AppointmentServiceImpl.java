package com.ganpo.medicalresource.service.impl;


import com.ganpo.medicalresource.entity.Appointment;
import com.ganpo.medicalresource.mapper.AppointmentMapper;
import com.ganpo.medicalresource.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public Appointment create(Appointment appointment) {
        // 生成 ticketNumber
        appointment.setTicketNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
        appointmentMapper.insert(appointment);
        return appointment;
    }

    @Override
    public Appointment getById(Long id) {
        return appointmentMapper.selectById(id);
    }
}
