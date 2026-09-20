package com.atguigu.medicalresource.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;


@Configuration
public class WebConfig {
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public FilterRegistrationBean<Filter> jwtFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter);

        registration.addUrlPatterns("/*");
        registration.setName("jwtFilter");
        registration.setOrder(1);
        return registration;
    }
}