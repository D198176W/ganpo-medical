package com.atguigu.healthmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IHealthPlanService<HealthPlan> extends IService<HealthPlan> {
    List<HealthPlan> getUserPlans(Long userId);
    boolean updatePlanProgress(Long planId, int progress);
}