package com.joy.vertx.agent.handler.input;

import com.joy.vertx.agent.handler.AgentHandler;

public interface InputAgentHandler extends AgentHandler {

	public abstract String startDeployment();
	
	public abstract String stopDeployment();
	
}
