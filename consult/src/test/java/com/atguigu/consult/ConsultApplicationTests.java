package com.atguigu.consult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "DASHSCOPE_API_KEY", matches = ".+")
@EnabledIfEnvironmentVariable(named = "PINECONE_API_KEY", matches = ".+")
class ConsultApplicationTests {

	@Test
	void contextLoads() {
	}

}
