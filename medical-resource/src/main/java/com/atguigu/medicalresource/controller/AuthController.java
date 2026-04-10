package com.atguigu.medicalresource.controller;


import com.atguigu.medicalresource.dto.ApiResponse;
import com.atguigu.medicalresource.entity.User;
import com.atguigu.medicalresource.service.UserService;
import com.atguigu.medicalresource.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.web.bind.annotation.*;

>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse<Map<String,Object>> register(@RequestBody @Valid User req) {
        if (userService.findByUsername(req.getUsername()) != null) {
            return ApiResponse.fail("用户名已存在");
        }
<<<<<<< HEAD
        String hashed = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt());//密码加密
        req.setPassword(hashed);
        userService.createUser(req);
        Map<String,Object> data = new HashMap<>();
        System.out.println(req.getId());
=======
        String hashed = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt());
        req.setPassword(hashed);
        userService.createUser(req);
        Map<String,Object> data = new HashMap<>();
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
        data.put("id", req.getId());
        return ApiResponse.ok(data);
    }

    @PostMapping("/login")
    public ApiResponse<Map<String,Object>> login(@RequestBody Map<String,String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) {
            return ApiResponse.fail("用户名/密码不能为空");
        }
        User u = userService.findByUsername(username);
<<<<<<< HEAD
        if (u == null){
            return ApiResponse.fail("用户不存在");
        }
=======
        if (u == null) return ApiResponse.fail("用户不存在");
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
        if (!BCrypt.checkpw(password, u.getPassword())) {
            return ApiResponse.fail("密码错误");
        }
        String token = jwtUtil.generateToken(u.getUsername());
        Map<String,Object> data = new HashMap<>();
<<<<<<< HEAD
        //登录之后，把token和user放入map集合
=======
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
        data.put("token", token);
        data.put("user", u);
        return ApiResponse.ok(data);
    }
}