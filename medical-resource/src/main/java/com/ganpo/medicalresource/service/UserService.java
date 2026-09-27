package com.ganpo.medicalresource.service;


import com.ganpo.medicalresource.entity.User;

public interface UserService {
    User findByUsername(String username);
    User createUser(User user);
    User findById(Long id);
}
