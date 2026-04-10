package com.atguigu.medicalresource.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
=======
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d

@Configuration
public class WebConfig {
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public FilterRegistrationBean<Filter> jwtFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter);
<<<<<<< HEAD
        // 拦截所有请求
=======
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
        registration.addUrlPatterns("/*");
        registration.setName("jwtFilter");
        registration.setOrder(1);
        return registration;
    }
}