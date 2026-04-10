package com.atguigu.medicalresource.service;


import com.atguigu.medicalresource.entity.ReimbursementRule;

import java.util.Optional;

public interface ReimbursementService {
    Optional<ReimbursementRule> getRuleByCrowd(String crowdType);
    ReimbursementResult calculate(String crowdType, double totalAmount, java.util.List<Breakdown> breakdowns);

    class Breakdown {
        public String type;
        public double amount;
    }

    class ReimbursementResult {
        public double reimbursableAmount;
        public java.util.List<Detail> details;
        public double limitPerYear;

        public static class Detail {
            public String type;
            public int ratio;
            public double reimbursedAmount;
        }
    }
}
