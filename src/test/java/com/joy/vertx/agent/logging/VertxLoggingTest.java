package com.joy.vertx.agent.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;

public class VertxLoggingTest extends AbstractVerticle {

	private static final Logger logger = LoggerFactory.getLogger(VertxLoggingTest.class);

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Launcher.main(new String[] { "run", VertxLoggingTest.class.getName() });
	}

	@Override
	public void start() throws Exception {

		HttpServer server = vertx.createHttpServer(new HttpServerOptions().setSsl(true)
				.setKeyStoreOptions(new JksOptions().setPath("server-keystore.jks").setPassword("wibble")));

		server.requestHandler(req -> {
			req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hell!</h1></body></html>");
		}).listen(4443);
		
		logger.trace(">>> started with trace");
        logger.debug(">>> started with debug");
        logger.info(">>> started with info");
        logger.warn(">>> started with warn");
        logger.error(">>> started with error");
	}
}
