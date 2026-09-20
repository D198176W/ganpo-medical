package com.atguigu.medicalresource.controller;

import com.atguigu.medicalresource.dto.ApiResponse;
import com.atguigu.medicalresource.entity.Hospital;
import com.atguigu.medicalresource.service.HospitalService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @GetMapping

    public ApiResponse<Page<Hospital>> list(@RequestParam(value = "page",defaultValue = "1") int page,
                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                            @RequestParam(value = "departmentId", required = false) String departmentId,
                                            @RequestParam(value = "sort", required = false) String sort,
                                            @RequestParam(value = "lat", required = false) Double lat,
                                            @RequestParam(value = "lng",required = false) Double lng,
                                            @RequestParam(value = "q", required = false) String q) {

        Page<Hospital> p = hospitalService.pageList(page, size, departmentId, sort, lat, lng, q);
        return ApiResponse.ok(p);
    }

    @GetMapping("/{id}")

    public ApiResponse<Hospital> detail(@PathVariable("id") Long id) {
        Hospital h = hospitalService.getById(id);
        if (h == null) {
            return ApiResponse.fail("医院不存在");
        }


        return ApiResponse.ok(h);
    }
}