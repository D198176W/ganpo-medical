package com.ganpo.pathology.service.impl;

import com.ganpo.pathology.dto.AuthRequest;
import com.ganpo.pathology.dto.AuthResponse;
import com.ganpo.pathology.entity.User;
import com.ganpo.pathology.mapper.UserMapper;
import com.ganpo.pathology.service.AuthService;
import com.ganpo.pathology.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
//    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserMapper userMapper, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse login(AuthRequest req) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", req.getUsername());
        User u = userMapper.selectOne(qw);
        if (u == null) throw new RuntimeException("用户不存在");
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) throw new RuntimeException("密码错误");
        String token = jwtUtil.generateToken(u.getUsername(), u.getId(), u.getRole());
        return new AuthResponse(token);
    }

    @Override
    public User register(String username, String password) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        if (userMapper.selectOne(qw) != null) throw new RuntimeException("用户名已存在");
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole("user");
        u.setVerified(false);
        userMapper.insert(u);
        return u;
    }
}
