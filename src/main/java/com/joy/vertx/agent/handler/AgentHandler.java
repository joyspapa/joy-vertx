package com.joy.vertx.agent.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joy.vertx.agent.common.AgentThreadLocalContext;

public interface AgentHandler {
	public static final Logger logger = LoggerFactory.getLogger(AgentHandler.class);
	
	/**
	 * 서비스의 전 처리
	 * @throws Exception
	 */
	public static void setUp(String serviceName) {
		AgentThreadLocalContext.elapsedTime.set(System.currentTimeMillis());
		AgentThreadLocalContext.serviceName.set(serviceName);
	}

	/**
	 * 서비스의 후 처리
	 * @throws Exception
	 */
	public static void tearDown() {
		logger.info("# [" + AgentThreadLocalContext.serviceName.get() + "] Total Elapsed Time : " + (System.currentTimeMillis() - AgentThreadLocalContext.elapsedTime.get())
				+ "ms");
		// TODO - logging, transaction closing, cache clear
		AgentThreadLocalContext.elapsedTime.remove();
		AgentThreadLocalContext.serviceName.remove();
	}
}
