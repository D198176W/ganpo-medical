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
        queryWrapper.eq(Appointment::getIdCard,appointment.getUsername());
        queryWrapper.eq(Appointment::getDepartment,appointment.getDepartment());
        queryWrapper.eq(Appointment::getDate,appointment.getDate());
        queryWrapper.eq(Appointment::getTime,appointment.getTime());

        Appointment appointmentDB = baseMapper.selectOne(queryWrapper);
        return appointmentDB;


    }

}
