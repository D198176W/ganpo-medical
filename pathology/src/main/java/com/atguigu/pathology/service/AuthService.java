package com.atguigu.pathology.service;

import com.atguigu.pathology.dto.AuthRequest;
import com.atguigu.pathology.dto.AuthResponse;
import com.atguigu.pathology.entity.User;

public interface AuthService {
    AuthResponse login(AuthRequest req);
    User register(String username, String password);
}
