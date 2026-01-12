package com.atguigu.healthmanagement.service.impl;

import com.atguigu.healthmanagement.entity.Hospital;
import com.atguigu.healthmanagement.mapper.HospitalMapper;
import com.atguigu.healthmanagement.service.IHospitalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital>
        implements IHospitalService {
    @Override
    public List<Hospital> getHospitalsByCity(String city) {
        return null;
    }

    @Override
    public List<Hospital> getHospitalsByDistance(Double maxDistance) {
        return null;
    }

    @Override
    public List<Hospital> searchHospitals(String keyword) {
        return null;
    }

    @Override
    public List<Hospital> getNearestHospitals(int limit) {
        return null;
    }

    @Override
    public List<Hospital> getEmergencyHospitals() {
        return null;
    }

    @Override
    public List<Hospital> get24HourHospitals() {
        return null;
    }

    @Override
    public List<Hospital> getHospitalsByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Map<String, Object> getHospitalStatistics() {
        return null;
    }

    @Override
    public List<Hospital> getHospitalsNearby(Double latitude, Double longitude, Double radius) {
        return null;
    }

    @Override
    public List<Hospital> getPopularHospitals(Integer limit) {
        return null;
    }
}
