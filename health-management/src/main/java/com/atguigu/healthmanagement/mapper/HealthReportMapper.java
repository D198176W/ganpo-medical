package com.atguigu.healthmanagement.mapper;

import com.atguigu.healthmanagement.entity.HealthReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface HealthReportMapper extends BaseMapper<HealthReport> {

    /**
     * 根据用户ID和报告类型查询报告列表
     */
    @Select("SELECT * FROM health_reports WHERE user_id = #{userId} AND report_type = #{reportType} ORDER BY start_date DESC")
    List<HealthReport> selectByUserIdAndType(@Param("userId") Long userId, @Param("reportType") String reportType);

    /**
     * 根据用户ID和时间范围查询报告
     */
    @Select("SELECT * FROM health_reports WHERE user_id = #{userId} AND start_date >= #{startDate} AND end_date <= #{endDate} ORDER BY generated_at DESC")
    List<HealthReport> selectByUserIdAndDateRange(@Param("userId") Long userId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    /**
     * 根据健康等级查询报告
     */
    @Select("SELECT * FROM health_reports WHERE user_id = #{userId} AND health_grade = #{healthGrade} ORDER BY overall_score DESC")
    List<HealthReport> selectByHealthGrade(@Param("userId") Long userId, @Param("healthGrade") String healthGrade);

    /**
     * 获取用户的最新报告
     */
    @Select("SELECT * FROM health_reports WHERE user_id = #{userId} ORDER BY generated_at DESC LIMIT 1")
    HealthReport selectLatestReport(@Param("userId") Long userId);

    /**
     * 分页查询用户的报告
     */
    @Select("SELECT * FROM health_reports WHERE user_id = #{userId} ORDER BY generated_at DESC")
    IPage<HealthReport> selectPageByUserId(Page<HealthReport> page, @Param("userId") Long userId);

    /**
     * 统计用户的报告数量
     */
    @Select("SELECT COUNT(*) FROM health_reports WHERE user_id = #{userId}")
    Long countByUserId(@Param("userId") Long userId);

    /**
     * 获取用户的健康评分趋势
     */
    @Select("SELECT start_date, overall_score FROM health_reports WHERE user_id = #{userId} ORDER BY start_date ASC")
    List<Map<String, Object>> selectScoreTrend(@Param("userId") Long userId);

    /**
     * 根据报告状态查询
     */
    @Select("SELECT * FROM health_reports WHERE user_id = #{userId} AND report_status = #{status} ORDER BY generated_at DESC")
    List<HealthReport> selectByStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * 更新报告状态
     */
    @Select("UPDATE health_reports SET report_status = #{status} WHERE id = #{id}")
    int updateReportStatus(@Param("id") Long id, @Param("status") String status);
}

