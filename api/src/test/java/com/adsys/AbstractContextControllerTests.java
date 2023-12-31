package com.adsys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@ContextConfiguration({ "classpath:spring/ApplicationContext-main.xml", "classpath:spring/ApplicationContext-dataSource.xml",
		"classpath:spring/ApplicationContext-redis.xml" })
public class AbstractContextControllerTests {

	@Autowired
	protected WebApplicationContext wac;
}
