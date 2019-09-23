package com.joy.vertx.agent.web.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;

public class TestMain {
	private static final Logger logger = LoggerFactory.getLogger(TestMain.class);

	public static void main(String[] args) {
		TestMain testMain = new TestMain();

		// http
		//testMain.testHttpServerVerticle();

		// https
		testMain.testHttpsServerVerticle();
	}

	private void testHttpServerVerticle() {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new AgentWebServerHttpVerticleTest(), asyncResult -> {
			deployMsg(asyncResult);
		});

	}

	private void testHttpsServerVerticle() {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new AgentWebServerHttpsVerticleTest(), asyncResult -> {
			deployMsg(asyncResult);
		});

	}

	private void deployMsg(AsyncResult<String> result) {
		//System.out.println("BasicVerticle deployment complete");
		logger.debug("success : " + result.succeeded());
	}
}
