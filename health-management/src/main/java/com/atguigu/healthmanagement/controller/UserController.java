package com.atguigu.healthmanagement.controller;

import com.atguigu.healthmanagement.dto.PasswordUpdateRequest;
import com.atguigu.healthmanagement.dto.UserStats;
import com.atguigu.healthmanagement.entity.User;
import com.atguigu.healthmanagement.service.IUserService;
import com.atguigu.healthmanagement.util.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 获取所有用户列表（管理员权限）
     */
    @GetMapping
    public Result<List<User>> getAllUsers() {
        try {
            List<User> users = userService.list();
            return Result.success(users);
        } catch (Exception e) {
           log.error("获取用户列表失败", e);
            return Result.error("获取用户列表失败");
        }
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getById(id);
            if (user != null) {
                user.setPasswordHash(null);
                // 隐藏密码信息
                return Result.success(user);
            } else {
                return Result.notFound("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败");
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            // 不更新密码字段
            user.setPasswordHash(null);

            boolean success = userService.updateById(user);
            if (success) {
                log.info("更新用户信息成功: {}", id);
                return Result.success(user, "更新成功");
            } else {
                return Result.error("更新用户信息失败");
            }
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新用户信息失败");
        }
    }

    /**
     * 更新用户密码
     */
    @PutMapping("/{id}/password")
    public Result<Boolean> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest request) {
        try {
            boolean success = userService.updatePassword(id, request.getOldPassword(), request.getNewPassword());
            if (success) {
                log.info("更新用户密码成功: {}", id);
                return Result.success(true, "密码更新成功");
            } else {
                return Result.error("密码更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户密码失败", e);
            return Result.error("密码更新失败");
        }
    }

    /**
     * 删除用户（管理员权限）
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(@PathVariable Long id) {
        try {
            boolean success = userService.removeById(id);
            if (success) {
                log.info("删除用户成功: {}", id);
                return Result.success(true, "删除成功");
            } else {
                return Result.error("删除用户失败");
            }
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return Result.error("删除用户失败");
        }
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/{id}/stats")
    public Result<UserStats> getUserStats(@PathVariable Long id) {
        try {
            UserStats stats = userService.getUserStats(id);
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取用户统计信息失败", e);
            return Result.error("获取统计信息失败");
        }
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    public Result<List<User>> searchUsers(@RequestParam String keyword) {
        try {
            List<User> users = userService.searchUsers(keyword);
            // 隐藏密码信息
            users.forEach(user -> user.setPasswordHash(null));
            return Result.success(users);
        } catch (Exception e) {
            log.error("搜索用户失败", e);
            return Result.error("搜索失败");
        }
    }

    // 请求参数类
//    @Data
//    public static class PasswordUpdateRequest {
//        private String oldPassword;
//        private String newPassword;
//    }

    // 用户统计信息类
//    @Data
//    public static class UserStats {
//        private Long totalRecords;
//        private Long totalReports;
//        private Integer continuousDays;
//        private Double healthScore;
//
//        public UserStats(Long totalRecords, Long totalReports, Integer continuousDays, Double healthScore) {
//            this.totalRecords = totalRecords;
//            this.totalReports = totalReports;
//            this.continuousDays = continuousDays;
//            this.healthScore = healthScore;
//        }
//    }
}
