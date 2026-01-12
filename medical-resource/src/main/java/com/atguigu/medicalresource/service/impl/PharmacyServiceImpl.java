package com.atguigu.medicalresource.service.impl;


import com.atguigu.medicalresource.entity.Pharmacy;
import com.atguigu.medicalresource.mapper.PharmacyMapper;
import com.atguigu.medicalresource.service.PharmacyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PharmacyServiceImpl implements PharmacyService {
    @Autowired
    PharmacyMapper pharmacyMapper;

    @Override
    public Page<Pharmacy> nearby(int page, int size, Double lat, Double lng, Double radius, String q) {
        QueryWrapper<Pharmacy> wrapper = new QueryWrapper<>();
        if (q != null && !q.isEmpty()) {
            wrapper.like("name", q).or().like("address", q);
        }
        Page<Pharmacy> p = new Page<>(page, size);
        return pharmacyMapper.selectPage(p, wrapper);
    }
}

