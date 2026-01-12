//package com.atguigu.healthmanagement.controller;
//
//import com.atguigu.healthmanagement.entity.User;
//import com.atguigu.healthmanagement.service.IUserService;
//import com.atguigu.healthmanagement.util.Result;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.crypto.SecretKey;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final IUserService userService;
//    private final PasswordEncoder passwordEncoder;
//
//    /**
//     * 用户登录
//     */
//    @PostMapping("/login")
//    public Result<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
//        try {
//            User user = userService.findByUsername(loginRequest.getUsername());
//            if (user == null) {
//                return Result.error("用户不存在");
//            }
//
//            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
//                return Result.error("密码错误");
//            }
//
//            // 生成JWT令牌
//            String token = generateToken(user);
//
//            Map<String, Object> result = new HashMap<>();
//            result.put("token", token);
//            result.put("user", user);
//            result.put("expiresIn", 86400000L); // 24小时
//
//            log.info("用户登录成功: {}", user.getUsername());
//            return Result.success(result);
//        } catch (Exception e) {
//            log.error("登录失败", e);
//            return Result.error("登录失败");
//        }
//    }
//
//    /**
//     * 用户注册
//     */
//    @PostMapping("/register")
//    public Result<User> register(@RequestBody RegisterRequest registerRequest) {
//        try {
//            // 检查用户名是否已存在
//            if (userService.findByUsername(registerRequest.getUsername()) != null) {
//                return Result.error("用户名已存在");
//            }
//
//            User user = new User();
//            user.setUsername(registerRequest.getUsername());
//            user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
//            user.setEmail(registerRequest.getEmail());
//            user.setPhone(registerRequest.getPhone());
//            user.setUsername(registerRequest.getRealName());
//            user.setGender(String.valueOf(registerRequest.getGender()));
//            user.setBirthDate(registerRequest.getBirthDate());
//
//            boolean success = userService.save(user);
//            if (success) {
//                log.info("用户注册成功: {}", user.getUsername());
//                return Result.success(user, "注册成功");
//            } else {
//                return Result.error("注册失败");
//            }
//        } catch (Exception e) {
//            log.error("注册失败", e);
//            return Result.error("注册失败");
//        }
//    }
//
//    /**
//     * 获取当前用户信息
//     */
//    @GetMapping("/me")
//    public Result<User> getCurrentUser(@RequestHeader("Authorization") String token) {
//        try {
//            String username = extractUsernameFromToken(token);
//            User user = userService.findByUsername(username);
//            if (user != null) {
//                return Result.success(user);
//            } else {
//                return Result.error("用户不存在");
//            }
//        } catch (Exception e) {
//            log.error("获取用户信息失败", e);
//            return Result.error("获取用户信息失败");
//        }
//    }
//
//    /**
//     * 生成JWT令牌
//     */
//    private String generateToken(User user) {
//        SecretKey key = Keys.hmacShaKeyFor("your-secret-key".getBytes());
//
//        return Jwts.builder()
//                .setSubject(user.getUsername())
//                .claim("userId", user.getId())
//                .claim("realName", user.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000L))
//                .signWith(key)
//                .compact();
//    }
//
//    /**
//     * 从令牌中提取用户名
//     */
//    private String extractUsernameFromToken(String token) {
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//
//        SecretKey key = Keys.hmacShaKeyFor("your-secret-key".getBytes());
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    // 请求参数类
//    @Data
//    public static class LoginRequest {
//        private String username;
//        private String password;
//    }
//
//    @Data
//    public static class RegisterRequest {
//        private String username;
//        private String password;
//        private String email;
//        private String phone;
//        private String realName;
//        private String gender;
//        private LocalDateTime birthDate;
//    }
//}


package com.atguigu.healthmanagement.controller;

import com.atguigu.healthmanagement.entity.User;
import com.atguigu.healthmanagement.service.IUserService;
import com.atguigu.healthmanagement.util.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.LocalDate;  // 改为 LocalDate
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    // JWT 密钥，建议配置在 application.yml 中
    private final String JWT_SECRET = "your-secret-key-must-be-at-least-256-bits-long";
    private final SecretKey JWT_KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.findByUsername(loginRequest.getUsername());
            if (user == null) {
                return Result.error("用户不存在");
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                return Result.error("密码错误");
            }

            // 生成JWT令牌
            String token = generateToken(user);

            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", user);
            result.put("expiresIn", 86400000L); // 24小时

            log.info("用户登录成功: {}", user.getUsername());
            return Result.success(result);
        } catch (Exception e) {
            log.error("登录失败", e);
            return Result.error("登录失败");
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterRequest registerRequest) {
        try {
            // 检查用户名是否已存在
            if (userService.findByUsername(registerRequest.getUsername()) != null) {
                return Result.error("用户名已存在");
            }

            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
            user.setEmail(registerRequest.getEmail());
            user.setPhone(registerRequest.getPhone());
            // 修复：使用正确的字段名
            user.setRealName(registerRequest.getRealName());  // 不是 setUsername
            user.setGender(registerRequest.getGender());  // 直接使用 String
            user.setBirthDate(registerRequest.getBirthDate().atStartOfDay());

            boolean success = userService.save(user);
            if (success) {
                log.info("用户注册成功: {}", user.getUsername());
                return Result.success(user, "注册成功");
            } else {
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            log.error("注册失败", e);
            return Result.error("注册失败");
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public Result<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String username = extractUsernameFromToken(token);
            User user = userService.findByUsername(username);
            if (user != null) {
                return Result.success(user);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败");
        }
    }

    /**
     * 生成JWT令牌
     */
    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("realName", user.getRealName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L))
                .signWith(JWT_KEY)
                .compact();
    }

    /**
     * 从令牌中提取用户名
     */
    private String extractUsernameFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Token解析失败", e);
            throw new RuntimeException("无效的Token");
        }
    }

    // 请求参数类
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
        private String phone;
        private String realName;
        private String gender;  // 改为 String 类型
        private LocalDate birthDate;  // 建议使用 LocalDate 而不是 LocalDateTime
    }
}
