package com.atguigu.medicalresource.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.atguigu.medicalresource.entity.Pharmacy;

public interface PharmacyService {
    Page<Pharmacy> nearby(int page, int size, Double lat, Double lng, Double radius, String q);
}
