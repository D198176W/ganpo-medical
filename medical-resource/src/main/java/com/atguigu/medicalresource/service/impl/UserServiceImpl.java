package com.atguigu.medicalresource.service.impl;


import com.atguigu.medicalresource.entity.User;
import com.atguigu.medicalresource.mapper.UserMapper;
import com.atguigu.medicalresource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> q = new QueryWrapper<>();
        q.eq("username", username);
        return userMapper.selectOne(q);
    }

    @Override
    public User createUser(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }
}