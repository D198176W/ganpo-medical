package com.atguigu.healthmanagement.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;
    private Long expiration;
    private String header;
    private String prefix;

    /**
     * 生成JWT密钥
     */
    @Bean
    public SecretKey secretKey() {
        if (secret == null || secret.trim().isEmpty()) {
            // 生成安全的随机密钥
            return Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
        // 使用配置的密钥
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 获取过期时间（毫秒）
     */
    public Long getExpirationMillis() {
        return expiration != null ? expiration : 86400000L; // 默认24小时
    }

    /**
     * 获取请求头名称
     */
    public String getHeader() {
        return header != null ? header : "Authorization";
    }

    /**
     * 获取Token前缀
     */
    public String getPrefix() {
        return prefix != null ? prefix : "Bearer ";
    }
}

