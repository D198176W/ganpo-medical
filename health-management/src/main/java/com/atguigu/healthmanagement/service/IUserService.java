package com.atguigu.healthmanagement.service;

import com.atguigu.healthmanagement.controller.UserController;
import com.atguigu.healthmanagement.dto.UserStats;
import com.atguigu.healthmanagement.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    User findByEmail(String email);

    /**
     * 根据手机号查找用户
     */
    User findByPhone(String phone);

    /**
     * 验证用户密码
     */
    boolean validatePassword(Long userId, String password);

    /**
     * 更新用户密码
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置用户密码
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 更新用户最后登录时间
     */
    boolean updateLastLoginTime(Long userId);

    /**
     * 搜索用户
     */
    List<User> searchUsers(String keyword);

    /**
     * 检查用户名是否已存在
     */
    boolean isUsernameExists(String username);

    /**
     * 检查邮箱是否已存在
     */
    boolean isEmailExists(String email);

    /**
     * 获取用户统计信息
     */
    Map<String, Object> getUserStatistics(Long userId);

    /**
     * 获取用户简要信息（不包含敏感信息）
     */
    User getUserProfile(Long userId);

    /**
     * 更新用户个人资料
     */
    boolean updateUserProfile(Long userId, User userProfile);

    /**
     * 批量更新用户状态
     */
    boolean batchUpdateUserStatus(List<Long> userIds, Integer status);

    /**
     * 获取活跃用户列表
     */
    List<User> getActiveUsers(int days);

    /**
     * 验证用户账户状态
     */
    boolean validateUserStatus(Long userId);



    UserStats getUserStats(Long id);
}
