package com.atguigu.healthmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.atguigu.healthmanagement.mapper")
public class HealthManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthManagementApplication.class, args);
		System.out.println("健康管理模块启动成功！");
		System.out.println("API文档: http://localhost:8081/swagger-ui.html");
	}
}
