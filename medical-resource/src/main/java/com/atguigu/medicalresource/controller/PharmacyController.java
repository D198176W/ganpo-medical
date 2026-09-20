package com.atguigu.medicalresource.controller;

import com.atguigu.medicalresource.dto.ApiResponse;
import com.atguigu.medicalresource.entity.Pharmacy;
import com.atguigu.medicalresource.service.PharmacyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {
    @Autowired
    private PharmacyService pharmacyService;

    @GetMapping

    public ApiResponse<Page<Pharmacy>> nearby(@RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "size",defaultValue = "10") int size,
                                              @RequestParam(value = "lat",required = false) Double lat,
                                              @RequestParam(value ="lng",required = false) Double lng,
                                              @RequestParam(value = "radius",required = false) Double radius,
                                              @RequestParam(value = "q",required = false) String q) {

        Page<Pharmacy> p = pharmacyService.nearby(page, size, lat, lng, radius, q);
        return ApiResponse.ok(p);
    }
}