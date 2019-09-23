package com.joy.vertx.agent.handler.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joy.vertx.agent.config.AgentConfig;
import com.joy.vertx.agent.handler.AgentHandler;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class CommonServiceHandler implements AgentHandler {

	private static final Logger logger = LoggerFactory.getLogger(CommonServiceHandler.class);

	public static void handleReloadConfig(RoutingContext routingContext) {
		try {
			logger.debug("debug msg");
			HttpServerResponse httpServerResponse = routingContext.response();
			httpServerResponse.setChunked(true);
			httpServerResponse.putHeader("Content-Type", "application/text").end("Success");
			AgentConfig.reloadConfig();

			

		} catch (Exception ex) {
			logger.warn("Error in initializing the AgentConfig.reloadConfig()", ex);
			
		}
	}

}