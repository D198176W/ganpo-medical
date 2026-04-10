package com.atguigu.medicalresource.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expire-seconds}") //3600秒，即过期时间一个小时
    private long expireSeconds;

/**
 * 生成并返回一个HMAC-SHA密钥
 * 该方法使用预定义的secret字符串通过UTF-8编码转换为字节数组
 * 然后使用Keys工具类生成HMAC-SHA密钥
 *
 * @return SecretKey 返回一个基于HMAC-SHA算法的密钥对象
 */
    private SecretKey getKey() {
    // 使用预定义的secret字符串，通过UTF-8编码转换为字节数组
    // 然后通过Keys工具类生成HMAC-SHA密钥
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
 * 生成JWT令牌
 * @param username 用户名
 * @return JWT令牌字符串
 */
    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expireSeconds * 1000);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("生成的token："+token);
        return token;
    }

/**
 * 解析JWT令牌并提取主题信息
 * @param token JWT令牌字符串
 * @return 令牌中包含的主题信息
 */
    public String parseToken(String token) {
//        Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
//        return claims.getSubject();
        Claims claims = Jwts.parserBuilder()   // ⚠️ 改这里
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        //System.out.println("执行解析逻辑");
       // System.out.println("注册时的用户名"+claims.getSubject());
        //返回注册时的用户名
        return claims.getSubject();
    }
}