package com.joy.vertx.agent.logging;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentLoggingTest {
	private static final Logger logger = LoggerFactory.getLogger(AgentLoggingTest.class);
	
	@Test
	public void log() {
		logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
	}
}
