package com.atguigu.medicalresource.service;


import com.atguigu.medicalresource.entity.User;

public interface UserService {
    User findByUsername(String username);
    User createUser(User user);
    User findById(Long id);
}
