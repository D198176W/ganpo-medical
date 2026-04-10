package com.atguigu.medicalresource.controller;


<<<<<<< HEAD
=======

>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
import com.atguigu.medicalresource.dto.ApiResponse;
import com.atguigu.medicalresource.entity.Department;
import com.atguigu.medicalresource.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.web.bind.annotation.*;

>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d

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


