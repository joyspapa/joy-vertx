package com.joy.vertx.agent.web.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.Router;

public interface AgentWebServerTest {
	static final Logger logger = LoggerFactory.getLogger(AgentWebServerTest.class);
	static final String encode = "UTF-8";

	default void setVariable() {
		String os = System.getProperty("os.name");
		logger.debug("OS name : " + os);
	}
	
	default void routeService(Router router) {
		router.get("/").handler(routinghandlerContext -> {
			routinghandlerContext.response().putHeader("content-type", "text/html")
					.end("<html><body><h1>GET Test!</h1></body></html>");
		});

		router.post("/").handler(routinghandlerContext -> {
			String bodyJson = routinghandlerContext.getBodyAsString(encode);
			//			if(bodyJson != null) {
			//				JsonObject jsonObject = new JsonObject(bodyJson);
			//				logger.info("jsonObject.toString() : " + jsonObject.toString());
			//			}
			logger.info("request body : " + bodyJson);
			routinghandlerContext.response().putHeader("content-type", "text/html")
					.end("<html><body><h1>Web Server Response!</h1></body></html>");
		});
	}
}
