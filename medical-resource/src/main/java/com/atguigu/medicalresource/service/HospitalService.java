package com.atguigu.medicalresource.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.atguigu.medicalresource.entity.Hospital;

public interface HospitalService {
    Page<Hospital> pageList(int page, int size, String departmentId, String sort, Double lat, Double lng, String q);
    Hospital getById(Long id);
}
