package com.atguigu.healthmanagement.mapper;

import com.atguigu.healthmanagement.entity.Hospital;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface HospitalMapper extends BaseMapper<Hospital> {

    /**
     * 根据城市查询医院
     */
    @Select("SELECT * FROM hospitals WHERE address LIKE CONCAT('%', #{city}, '%') ORDER BY distance ASC")
    List<Hospital> selectByCity(@Param("city") String city);

    /**
     * 根据距离范围查询医院（公里）
     */
    @Select("SELECT * FROM hospitals WHERE distance <= #{maxDistance} ORDER BY distance ASC")
    List<Hospital> selectByDistance(@Param("maxDistance") Double maxDistance);

    /**
     * 查询有急诊科的医院
     */
    @Select("SELECT * FROM hospitals WHERE emergency_department = true ORDER BY distance ASC")
    List<Hospital> selectEmergencyHospitals();

    /**
     * 根据医院名称模糊搜索
     */
    @Select("SELECT * FROM hospitals WHERE name LIKE CONCAT('%', #{keyword}, '%') ORDER BY distance ASC")
    List<Hospital> searchByName(@Param("keyword") String keyword);

    /**
     * 获取最近的医院（按距离排序）
     */
    @Select("SELECT * FROM hospitals ORDER BY distance ASC LIMIT #{limit}")
    List<Hospital> selectNearestHospitals(@Param("limit") Integer limit);

    /**
     * 根据营业状态查询医院
     */
    @Select("SELECT * FROM hospitals WHERE business_hours LIKE '%24小时%' OR business_hours LIKE '%全天%'")
    List<Hospital> select24HourHospitals();

    /**
     * 获取医院统计信息
     */
    @Select("SELECT COUNT(*) as total, " +
            "SUM(CASE WHEN emergency_department = true THEN 1 ELSE 0 END) as emergency_count, " +
            "AVG(distance) as avg_distance FROM hospitals")
    Map<String, Object> selectHospitalStats();

    /**
     * 根据多个ID查询医院
     */
    @Select("<script>" +
            "SELECT * FROM hospitals WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "ORDER BY distance ASC" +
            "</script>")
    List<Hospital> selectByIds(@Param("ids") List<Long> ids);
}