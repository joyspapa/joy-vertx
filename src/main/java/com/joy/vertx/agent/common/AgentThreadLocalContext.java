package com.joy.vertx.agent.common;

public class AgentThreadLocalContext {

	// 서비스 시간
	public static ThreadLocal<Long> elapsedTime = new ThreadLocal<Long>();

	// Service Name
	public static ThreadLocal<String> serviceName = new ThreadLocal<String>();
}
