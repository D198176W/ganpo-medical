package com.atguigu.medicalresource.controller;


import com.atguigu.medicalresource.dto.ApiResponse;
import com.atguigu.medicalresource.entity.ReimbursementRule;
import com.atguigu.medicalresource.service.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/reimbursement")
public class ReimbursementController {
    @Autowired
    private ReimbursementService reimbursementService;

    @GetMapping("/rules")

    public ApiResponse<List<ReimbursementRule>> rules(@RequestParam(value = "crowd", defaultValue = "student") String crowd) {
        Optional<ReimbursementRule> r = reimbursementService.getRuleByCrowd(crowd);
        if (!r.isPresent()){
            return ApiResponse.fail("未找到规则");
        }

        return ApiResponse.ok(Collections.singletonList(r.get()));
    }

    @PostMapping("/calculate")
    public ApiResponse<ReimbursementService.ReimbursementResult> calculate(@RequestBody Map<String,Object> body) {
        String crowd = (String) body.getOrDefault("userType", "student");
        Double totalAmount = ((Number) body.getOrDefault("totalAmount", 0)).doubleValue();
        List<Map<String,Object>> list = (List<Map<String,Object>>) body.getOrDefault("breakdown", new ArrayList<>());
        List<ReimbursementService.Breakdown> breakdowns = new ArrayList<>();
        for (Map<String,Object> m : list) {
            ReimbursementService.Breakdown b = new ReimbursementService.Breakdown();
            b.type = (String) m.get("type");
            b.amount = ((Number) m.getOrDefault("amount", 0)).doubleValue();
            breakdowns.add(b);
        }
        ReimbursementService.ReimbursementResult res = reimbursementService.calculate(crowd, totalAmount, breakdowns);
        return ApiResponse.ok(res);
    }
}