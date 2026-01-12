package com.atguigu.consult.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");  // 允许所有前端域名
        config.setAllowCredentials(true);     // 允许携带Cookie
        config.addAllowedMethod("*");         // 允许所有请求方法
        config.addAllowedHeader("*");         // 允许所有请求头
        config.setMaxAge(3600L);              // 预检请求有效期

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // 所有接口生效

        return new CorsFilter(source);
    }
}
