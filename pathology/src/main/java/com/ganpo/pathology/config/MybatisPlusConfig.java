package com.ganpo.pathology.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ganpo.pathology.mapper")
public class MybatisPlusConfig {
    // 默认配置即可，MyBatis-Plus 自动注入
}
