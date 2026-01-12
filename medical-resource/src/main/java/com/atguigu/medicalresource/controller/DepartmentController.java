package com.atguigu.medicalresource.controller;



import com.atguigu.medicalresource.dto.ApiResponse;
import com.atguigu.medicalresource.entity.Department;
import com.atguigu.medicalresource.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ApiResponse<List<Department>> list() {
        return ApiResponse.ok(departmentService.listAll());
    }
}


