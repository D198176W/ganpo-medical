package com.atguigu.healthmanagement.service;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.Map;

/**
 * JWT令牌服务接口
 */
public interface JwtService {

    /**
     * 生成JWT令牌
     */
    String generateToken(String username, Map<String, Object> claims);

    /**
     * 生成JWT令牌（带过期时间）
     */
    String generateToken(String username, Map<String, Object> claims, Long expiration);

    /**
     * 验证JWT令牌
     */
    boolean validateToken(String token);

    /**
     * 解析JWT令牌
     */
    Claims parseToken(String token);

    /**
     * 从令牌中获取用户名
     */
    String getUsernameFromToken(String token);

    /**
     * 从令牌中获取用户ID
     */
    Long getUserIdFromToken(String token);

    /**
     * 从令牌中获取过期时间
     */
    Date getExpirationDateFromToken(String token);

    /**
     * 检查令牌是否即将过期
     */
    boolean isTokenExpiringSoon(String token, int minutes);

    /**
     * 刷新JWT令牌
     */
    String refreshToken(String token);

    /**
     * 使令牌失效（加入黑名单）
     */
    boolean invalidateToken(String token);

    /**
     * 检查令牌是否在黑名单中
     */
    boolean isTokenBlacklisted(String token);

    /**
     * 获取剩余有效时间（秒）
     */
    Long getRemainingTime(String token);
}
