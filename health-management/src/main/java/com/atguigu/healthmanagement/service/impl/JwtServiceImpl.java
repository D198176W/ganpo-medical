package com.atguigu.healthmanagement.service.impl;

import com.atguigu.healthmanagement.config.JwtConfig;
import com.atguigu.healthmanagement.service.JwtService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtConfig jwtConfig;

    // 令牌黑名单（生产环境应使用Redis）
    private final Map<String, Date> tokenBlacklist = new ConcurrentHashMap<>();

    @Override
    public String generateToken(String username, Map<String, Object> claims) {
        return generateToken(username, claims, jwtConfig.getExpirationMillis());
    }

    @Override
    public String generateToken(String username, Map<String, Object> claims, Long expiration) {
        try {
            if (claims == null) {
                claims = new HashMap<>();
            }

            // 设置标准声明
            claims.putIfAbsent("username", username);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(jwtConfig.secretKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            log.error("生成JWT令牌失败: {}", username, e);
            throw new RuntimeException("令牌生成失败", e);
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                log.warn("令牌已在黑名单中: {}", token);
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.secretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT令牌已过期: {}", token);
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的JWT令牌: {}", token);
        } catch (MalformedJwtException e) {
            log.warn("JWT令牌格式错误: {}", token);
        } catch (SignatureException e) {
            log.warn("JWT令牌签名验证失败: {}", token);
        } catch (IllegalArgumentException e) {
            log.warn("JWT令牌参数错误: {}", token);
        } catch (Exception e) {
            log.error("验证JWT令牌时发生未知错误: {}", token, e);
        }
        return false;
    }

    @Override
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.secretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("解析JWT令牌失败: {}", token, e);
            throw new RuntimeException("令牌解析失败", e);
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.error("从令牌获取用户名失败: {}", token, e);
            return null;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            Object userIdObj = claims.get("userId");
            if (userIdObj != null) {
                if (userIdObj instanceof Long) {
                    return (Long) userIdObj;
                } else if (userIdObj instanceof Integer) {
                    return ((Integer) userIdObj).longValue();
                } else if (userIdObj instanceof String) {
                    return Long.parseLong((String) userIdObj);
                }
            }
            return null;
        } catch (Exception e) {
            log.error("从令牌获取用户ID失败: {}", token, e);
            return null;
        }
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            log.error("从令牌获取过期时间失败: {}", token, e);
            return null;
        }
    }

    @Override
    public boolean isTokenExpiringSoon(String token, int minutes) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            if (expiration == null) {
                return false;
            }

            long diff = expiration.getTime() - System.currentTimeMillis();
            return diff < (minutes * 60 * 1000L);
        } catch (Exception e) {
            log.error("检查令牌是否即将过期失败: {}", token, e);
            return false;
        }
    }

    @Override
    public String refreshToken(String token) {
        try {
            if (!validateToken(token)) {
                throw new RuntimeException("原始令牌无效");
            }

            Claims claims = parseToken(token);
            String username = claims.getSubject();

            // 移除敏感声明
            claims.remove("iat");
            claims.remove("exp");
            claims.remove("nbf");

            // 生成新令牌
            String newToken = generateToken(username, claims);

            // 将旧令牌加入黑名单
            invalidateToken(token);

            return newToken;
        } catch (Exception e) {
            log.error("刷新JWT令牌失败: {}", token, e);
            throw new RuntimeException("令牌刷新失败", e);
        }
    }

    @Override
    public boolean invalidateToken(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            if (expiration != null) {
                tokenBlacklist.put(token, expiration);
                log.info("令牌已加入黑名单: {}", token);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("使令牌失效失败: {}", token, e);
            return false;
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        Date expiration = tokenBlacklist.get(token);
        if (expiration != null) {
            if (expiration.before(new Date())) {
                // 过期令牌从黑名单中移除
                tokenBlacklist.remove(token);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public Long getRemainingTime(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            if (expiration == null) {
                return 0L;
            }

            long remaining = expiration.getTime() - System.currentTimeMillis();
            return remaining > 0 ? remaining / 1000 : 0L;
        } catch (Exception e) {
            log.error("获取令牌剩余时间失败: {}", token, e);
            return 0L;
        }
    }
}
