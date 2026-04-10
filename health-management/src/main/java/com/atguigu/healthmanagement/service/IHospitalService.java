package com.atguigu.healthmanagement.service;

import com.atguigu.healthmanagement.entity.Hospital;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 医院信息服务接口
 */
public interface IHospitalService extends IService<Hospital> {

    /**
     * 根据城市查询医院
     */
    List<Hospital> getHospitalsByCity(String city);

    /**
     * 根据距离查询附近的医院
     */
    List<Hospital> getHospitalsByDistance(Double maxDistance);

    /**
     * 搜索医院
     */
    List<Hospital> searchHospitals(String keyword);

    /**
     * 获取最近的医院
     */
    List<Hospital> getNearestHospitals(int limit);

    /**
     * 获取有急诊科的医院
     */
    List<Hospital> getEmergencyHospitals();

    /**
     * 获取24小时营业的医院
     */
    List<Hospital> get24HourHospitals();

    /**
     * 根据多个ID查询医院
     */
    List<Hospital> getHospitalsByIds(List<Long> ids);

    /**
     * 获取医院统计信息
     */
    Map<String, Object> getHospitalStatistics();

    /**
     * 根据经纬度查询附近的医院
     */
    List<Hospital> getHospitalsNearby(Double latitude, Double longitude, Double radius);

    List<Hospital> getPopularHospitals(Integer limit);
}

