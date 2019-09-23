package com.joy.vertx.agent.web.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class AgentWebServerHttpVerticleTest extends AbstractVerticle implements AgentWebServerTest {
	private static final Logger logger = LoggerFactory.getLogger(AgentWebServerHttpVerticleTest.class);

	private int port = 8092;

	public static void main(String[] args) {
		logger.info("Launcher.main ");
		Launcher.main(new String[] { "run", AgentWebServerHttpVerticleTest.class.getName() });
	}

	@Override
	public void start() throws Exception {
		setVariable();

		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());

		// Common service
		routeService(router);

		server.requestHandler(router::accept).listen(port);
		
		logger.info("... Server has been started with this port : " + port);
	}
}
