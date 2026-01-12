package com.atguigu.pathology.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.atguigu.pathology.mapper")
public class MybatisPlusConfig {
    // 默认配置即可，MyBatis-Plus 自动注入
}
