package com.ganpo.pathology.service;

import com.ganpo.pathology.dto.AuthRequest;
import com.ganpo.pathology.dto.AuthResponse;
import com.ganpo.pathology.entity.User;

public interface AuthService {
    AuthResponse login(AuthRequest req);
    User register(String username, String password);
}
