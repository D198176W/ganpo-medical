package com.atguigu.medicalresource.controller;


import com.atguigu.medicalresource.dto.ApiResponse;
import com.atguigu.medicalresource.entity.Appointment;
import com.atguigu.medicalresource.entity.User;
import com.atguigu.medicalresource.service.AppointmentService;
import com.atguigu.medicalresource.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<Appointment> create(@RequestBody Appointment req, HttpServletRequest request) {
        // 鉴权：从 JwtFilter 放入的 request attribute 中取出 username
        String username = (String) request.getAttribute("currentUser");
        if (username == null) return ApiResponse.fail("需要登录");
        User u = userService.findByUsername(username);
        if (u == null) return ApiResponse.fail("登录用户不存在");
        req.setUserId(u.getId());
        Appointment created = appointmentService.create(req);
        return ApiResponse.ok(created);
    }

    @GetMapping("/{id}")
    public ApiResponse<Appointment> get(@PathVariable Long id) {
        Appointment a = appointmentService.getById(id);
        if (a == null) return ApiResponse.fail("预约不存在");
        return ApiResponse.ok(a);
    }
}