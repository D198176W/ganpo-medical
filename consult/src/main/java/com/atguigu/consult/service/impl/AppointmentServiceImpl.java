package com.atguigu.consult.service.impl;


import com.atguigu.consult.entity.Appointment;
import com.atguigu.consult.mapper.AppointmentMapper;
import com.atguigu.consult.service.AppointmentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {

    @Override
    public Appointment getOne(Appointment appointment){
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Appointment::getUsername,appointment.getUsername());
        queryWrapper.eq(Appointment::getIdCard,appointment.getIdCard());
        queryWrapper.eq(Appointment::getDepartment,appointment.getDepartment());
        queryWrapper.eq(Appointment::getDate,appointment.getDate());
        queryWrapper.eq(Appointment::getTime,appointment.getTime());

        Appointment appointmentDB = baseMapper.selectOne(queryWrapper);
        return appointmentDB;


    }

    @Override
    public long countOccupiedSlots(String department, String date, String time, String doctorName) {
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Appointment::getDepartment, department);
        queryWrapper.eq(Appointment::getDate, date);
        queryWrapper.eq(Appointment::getTime, time);
        if (doctorName != null && !doctorName.isBlank()) {
            queryWrapper.eq(Appointment::getDoctorName, doctorName);
        }
        return baseMapper.selectCount(queryWrapper);
    }

}
