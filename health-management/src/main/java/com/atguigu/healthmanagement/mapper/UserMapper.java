package com.atguigu.healthmanagement.mapper;

import com.atguigu.healthmanagement.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM users WHERE email = #{email}")
    User selectByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     */
    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User selectByPhone(@Param("phone") String phone);

    /**
     * 更新用户最后登录时间
     */
    @Update("UPDATE users SET last_login_at = NOW() WHERE id = #{userId}")
    int updateLastLoginTime(@Param("userId") Long userId);

    /**
     * 更新用户密码
     */
    @Update("UPDATE users SET password_hash = #{passwordHash}, updated_at = NOW() WHERE id = #{userId}")
    int updatePassword(@Param("userId") Long userId, @Param("passwordHash") String passwordHash);

    /**
     * 搜索用户（根据用户名、真实姓名、邮箱）
     */
    @Select("SELECT * FROM users WHERE username LIKE CONCAT('%', #{keyword}, '%') " +
            "OR real_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR email LIKE CONCAT('%', #{keyword}, '%')")
    List<User> searchUsers(@Param("keyword") String keyword);

    /**
     * 统计用户数量
     */
    @Select("SELECT COUNT(*) as total_users FROM users")
    Long countTotalUsers();

    /**
     * 获取用户统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as total, " +
            "SUM(CASE WHEN created_at >= CURDATE() - INTERVAL 7 DAY THEN 1 ELSE 0 END) as new_users_7days, " +
            "SUM(CASE WHEN gender = 1 THEN 1 ELSE 0 END) as male_count, " +
            "SUM(CASE WHEN gender = 2 THEN 1 ELSE 0 END) as female_count " +
            "FROM users")
    Map<String, Object> selectUserStats();

    /**
     * 查询最近注册的用户
     */
    @Select("SELECT * FROM users ORDER BY created_at DESC LIMIT #{limit}")
    List<User> selectRecentUsers(@Param("limit") Integer limit);

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int existsByEmail(@Param("email") String email);

    /**
     * 更新用户状态
     */
    @Update("UPDATE users SET status = #{status}, updated_at = NOW() WHERE id = #{userId}")
    int updateUserStatus(@Param("userId") Long userId, @Param("status") Integer status);
}