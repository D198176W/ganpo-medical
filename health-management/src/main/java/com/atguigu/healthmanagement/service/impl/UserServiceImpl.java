package com.atguigu.healthmanagement.service.impl;

import com.atguigu.healthmanagement.controller.UserController;
import com.atguigu.healthmanagement.dto.UserStats;
import com.atguigu.healthmanagement.entity.User;
import com.atguigu.healthmanagement.mapper.UserMapper;
import com.atguigu.healthmanagement.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }

    @Override
    public User findByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return getOne(queryWrapper);
    }

    @Override
    public User findByPhone(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        return getOne(queryWrapper);
    }

    @Override
    public boolean validatePassword(Long userId, String password) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPasswordHash());
    }

    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            log.warn("用户不存在: {}", userId);
            return false;
        }

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            log.warn("旧密码验证失败: {}", userId);
            return false;
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    public boolean updateLastLoginTime(Long userId) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }

        // 这里假设User实体有lastLoginAt字段
        // user.setLastLoginAt(LocalDateTime.now());
        return updateById(user);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", keyword)
                .or().like("real_name", keyword)
                .or().like("email", keyword);
        return list(queryWrapper);
    }

    @Override
    public boolean isUsernameExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean isEmailExists(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return count(queryWrapper) > 0;
    }

    @Override
    public Map<String, Object> getUserStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // 这里需要根据实际业务需求实现统计逻辑
        // 例如：健康记录数量、报告数量、连续登录天数等

        try {
            // 示例统计信息
            stats.put("totalRecords", 0);
            stats.put("totalReports", 0);
            stats.put("continuousDays", 0);
            stats.put("healthScore", 85);
            stats.put("lastLogin", LocalDateTime.now().minusDays(1));

            return stats;
        } catch (Exception e) {
            log.error("获取用户统计信息失败: {}", userId, e);
            return stats;
        }
    }

    @Override
    public User getUserProfile(Long userId) {
        User user = getById(userId);
        if (user != null) {
            // 清除敏感信息
            user.setPasswordHash(null);
        }
        return user;
    }

    @Override
    public boolean updateUserProfile(Long userId, User userProfile) {
        User existingUser = getById(userId);
        if (existingUser == null) {
            return false;
        }

        // 只更新允许修改的字段，避免密码等敏感字段被修改
        existingUser.setUsername(userProfile.getUsername());
        existingUser.setEmail(userProfile.getEmail());
        existingUser.setPhone(userProfile.getPhone());
        existingUser.setGender(userProfile.getGender());
        existingUser.setBirthDate(userProfile.getBirthDate());

        return updateById(existingUser);
    }

    @Override
    public boolean batchUpdateUserStatus(List<Long> userIds, Integer status) {
        try {
            // 使用MyBatis Plus的批量更新
            userIds.forEach(userId -> {
                User user = new User();
                user.setId(userId);
                // 假设有status字段
                // user.setStatus(status);
                updateById(user);
            });
            return true;
        } catch (Exception e) {
            log.error("批量更新用户状态失败", e);
            return false;
        }
    }

    @Override
    public List<User> getActiveUsers(int days) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 假设有lastLoginAt字段
        // queryWrapper.ge("last_login_at", LocalDateTime.now().minusDays(days));
        queryWrapper.orderByDesc("created_at");
        return list(queryWrapper);
    }

    @Override
    public boolean validateUserStatus(Long userId) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }

        // 检查用户状态是否正常
        // 例如：是否被禁用、是否过期等

        return true; // 默认返回正常
    }

    @Override
    public UserStats getUserStats(Long id) {
        return null;
    }
}
