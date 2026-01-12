package com.atguigu.healthmanagement.controller;

import com.atguigu.healthmanagement.entity.Hospital;
import com.atguigu.healthmanagement.service.IHospitalService;
import com.atguigu.healthmanagement.util.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final IHospitalService hospitalService;

    /**
     * 获取所有医院列表
     */
    @GetMapping
    public Result<List<Hospital>> getAllHospitals() {
        try {
            List<Hospital> hospitals = hospitalService.list();
            return Result.success(hospitals);
        } catch (Exception e) {
            log.error("获取医院列表失败", e);
            return Result.error("获取医院列表失败");
        }
    }

    /**
     * 根据ID获取医院详情
     */
    @GetMapping("/{id}")
    public Result<Hospital> getHospitalById(@PathVariable Long id) {
        try {
            Hospital hospital = hospitalService.getById(id);
            if (hospital != null) {
                return Result.success(hospital);
            } else {
                return Result.notFound("医院不存在");
            }
        } catch (Exception e) {
            log.error("获取医院详情失败", e);
            return Result.error("获取医院详情失败");
        }
    }

    /**
     * 根据城市查询医院
     */
    @GetMapping("/city/{city}")
    public Result<List<Hospital>> getHospitalsByCity(@PathVariable String city) {
        try {
            List<Hospital> hospitals = hospitalService.getHospitalsByCity(city);
            return Result.success(hospitals);
        } catch (Exception e) {
            log.error("根据城市查询医院失败", e);
            return Result.error("查询失败");
        }
    }

    /**
     * 根据医院名称搜索
     */
    @GetMapping("/search")
    public Result<List<Hospital>> searchHospitals(@RequestParam String keyword) {
        try {
            List<Hospital> hospitals = hospitalService.searchHospitals(keyword);
            return Result.success(hospitals);
        } catch (Exception e) {
            log.error("搜索医院失败", e);
            return Result.error("搜索失败");
        }
    }

    /**
     * 创建医院信息（管理员权限）
     */
    @PostMapping
    public Result<Hospital> createHospital(@RequestBody Hospital hospital) {
        try {
            boolean success = hospitalService.save(hospital);
            if (success) {
                log.info("创建医院信息成功: {}", hospital.getName());
                return Result.success(hospital, "创建成功");
            } else {
                return Result.error("创建医院信息失败");
            }
        } catch (Exception e) {
            log.error("创建医院信息失败", e);
            return Result.error("创建医院信息失败");
        }
    }

    /**
     * 更新医院信息（管理员权限）
     */
    @PutMapping("/{id}")
    public Result<Hospital> updateHospital(@PathVariable Long id, @RequestBody Hospital hospital) {
        try {
            hospital.setId(id);
            boolean success = hospitalService.updateById(hospital);
            if (success) {
                log.info("更新医院信息成功: {}", id);
                return Result.success(hospital, "更新成功");
            } else {
                return Result.error("更新医院信息失败");
            }
        } catch (Exception e) {
            log.error("更新医院信息失败", e);
            return Result.error("更新医院信息失败");
        }
    }

    /**
     * 获取热门医院（按评分排序）
     */
    @GetMapping("/popular")
    public Result<List<Hospital>> getPopularHospitals(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Hospital> hospitals = hospitalService.getPopularHospitals(limit);
            return Result.success(hospitals);
        } catch (Exception e) {
            log.error("获取热门医院失败", e);
            return Result.error("获取失败");
        }
    }
}
