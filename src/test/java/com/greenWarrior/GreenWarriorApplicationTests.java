package com.greenWarrior;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:/application-junit.properties")
class GreenWarriorApplicationTests {

	@Test
	void contextLoads() {
	}

}
