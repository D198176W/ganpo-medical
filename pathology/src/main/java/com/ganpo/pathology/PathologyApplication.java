package com.ganpo.pathology;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ganpo.pathology.mapper")

//@EnableAsync
public class PathologyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PathologyApplication.class, args);
	}

}
