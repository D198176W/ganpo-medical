package com.atguigu.medicalresource.controller;

import com.atguigu.medicalresource.dto.ApiResponse;
import com.atguigu.medicalresource.entity.Pharmacy;
import com.atguigu.medicalresource.service.PharmacyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.web.bind.annotation.*;
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {
    @Autowired
    private PharmacyService pharmacyService;

    @GetMapping
<<<<<<< HEAD
    public ApiResponse<Page<Pharmacy>> nearby(@RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "size",defaultValue = "10") int size,
                                              @RequestParam(value = "lat",required = false) Double lat,
                                              @RequestParam(value ="lng",required = false) Double lng,
                                              @RequestParam(value = "radius",required = false) Double radius,
                                              @RequestParam(value = "q",required = false) String q) {
=======
    public ApiResponse<Page<Pharmacy>> nearby(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(required = false) Double lat,
                                              @RequestParam(required = false) Double lng,
                                              @RequestParam(required = false) Double radius,
                                              @RequestParam(required = false) String q) {
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
        Page<Pharmacy> p = pharmacyService.nearby(page, size, lat, lng, radius, q);
        return ApiResponse.ok(p);
    }
}