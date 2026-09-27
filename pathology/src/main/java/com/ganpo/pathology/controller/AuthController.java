package com.ganpo.pathology.controller;

import com.ganpo.pathology.dto.AuthRequest;
import com.ganpo.pathology.dto.AuthResponse;
import com.ganpo.pathology.entity.User;
import com.ganpo.pathology.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody AuthRequest req) {
        User u = authService.register(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(u);
    }
}
