package com.atguigu.medicalresource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("reimbursement_rules")
public class ReimbursementRule {
    private Long id;
    private String crowdType;
    private Integer outpatientRatio;
    private Integer inpatientRatio;
    private Integer examRatio;
    private Integer drugRatio;
    private Double maxPerYear;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}