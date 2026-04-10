package com.atguigu.medicalresource.service.impl;

import com.atguigu.medicalresource.entity.ReimbursementRule;
import com.atguigu.medicalresource.mapper.ReimbursementRuleMapper;
import com.atguigu.medicalresource.service.ReimbursementService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {
    @Autowired
    ReimbursementRuleMapper ruleMapper;

    @Override
    public Optional<ReimbursementRule> getRuleByCrowd(String crowdType) {
        QueryWrapper<ReimbursementRule> q = new QueryWrapper<>();
        q.eq("crowd_type", crowdType);
        ReimbursementRule r = ruleMapper.selectOne(q);
        return Optional.ofNullable(r);
    }

    @Override
    public ReimbursementResult calculate(String crowdType, double totalAmount, java.util.List<Breakdown> breakdowns) {
        ReimbursementResult res = new ReimbursementResult();
        ReimbursementRule rule = getRuleByCrowd(crowdType).orElse(null);
        if (rule == null) {
            throw new IllegalArgumentException("未找到对应的报销规则");
        }
        res.limitPerYear = rule.getMaxPerYear() == null ? 0 : rule.getMaxPerYear();
        res.details = new ArrayList<>();
        double totalReimb = 0;
        for (Breakdown b : breakdowns) {
            int ratio = 0;
            switch (b.type) {
                case "门诊":
                    ratio = rule.getOutpatientRatio() == null ? 0 : rule.getOutpatientRatio();
                    break;
                case "住院":
                    ratio = rule.getInpatientRatio() == null ? 0 : rule.getInpatientRatio();
                    break;
                case "检查":
                    ratio = rule.getExamRatio() == null ? 0 : rule.getExamRatio();
                    break;
                case "药品":
                    ratio = rule.getDrugRatio() == null ? 0 : rule.getDrugRatio();
                    break;
                default:
                    ratio = 0;
            }
            ReimbursementResult.Detail d = new ReimbursementResult.Detail();
            d.type = b.type;
            d.ratio = ratio;
            d.reimbursedAmount = Math.round(b.amount * ratio) / 100.0;
            totalReimb += d.reimbursedAmount;
            res.details.add(d);
        }
        if (totalReimb > res.limitPerYear) totalReimb = res.limitPerYear;
        res.reimbursableAmount = totalReimb;
        return res;
    }
}