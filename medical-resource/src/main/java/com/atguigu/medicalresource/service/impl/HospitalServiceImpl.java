package com.atguigu.medicalresource.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.atguigu.medicalresource.entity.Hospital;
import com.atguigu.medicalresource.mapper.HospitalMapper;
import com.atguigu.medicalresource.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalMapper hospitalMapper;

    @Override
    public Page<Hospital> pageList(int page, int size, String departmentId, String sort, Double lat, Double lng, String q) {
        QueryWrapper<Hospital> wrapper = new QueryWrapper<>();
        if (q != null && !q.isEmpty()) {
            wrapper.like("name", q);
        }
        if (departmentId != null && !"all".equals(departmentId)) {
            // 简化：用 core_departments 字段包含判断
            wrapper.like("core_departments", departmentId);
        }
        if ("level".equals(sort)) {
            wrapper.orderByDesc("level");
        } else if ("rating".equals(sort)) {
            wrapper.orderByDesc("rating");
        } else if ("availability".equals(sort)) {
            wrapper.orderByDesc("has_open_slots");
        } else {
            wrapper.orderByAsc("id"); // 默认
        }
        Page<Hospital> p = new Page<>(page, size);
        return hospitalMapper.selectPage(p, wrapper);
    }

    @Override
    public Hospital getById(Long id) {
        return hospitalMapper.selectById(id);
    }
}
