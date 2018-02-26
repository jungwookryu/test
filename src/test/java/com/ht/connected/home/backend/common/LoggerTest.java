package com.ht.connected.home.backend.common;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoggerTest {
	private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
	
	@Test
	public void consoleLogTest() {
		logger.info("infoConsoleTest");
		logger.debug("debugConsoleTest");
		logger.trace("traceConsoleTest");
	}

}
