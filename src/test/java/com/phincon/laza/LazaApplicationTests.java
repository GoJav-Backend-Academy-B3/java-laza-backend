package com.phincon.laza;

import com.phincon.laza.config.RestClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {RestClientConfig.class, RestTemplateAutoConfiguration.class})
class LazaApplicationTests {

	@Test
	void contextLoads() {
	}

}
