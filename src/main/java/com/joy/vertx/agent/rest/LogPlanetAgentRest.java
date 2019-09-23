package com.joy.vertx.agent.rest;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joy.vertx.agent.config.AgentConfig;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class LogPlanetAgentRest extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(LogPlanetAgentRest.class);
	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		logger.debug("aaaa");
		System.out.println("aaaa");
		Launcher.main(new String[] { "run", LogPlanetAgentRest.class.getName() });
	}

	@Override
	public void start(Future<Void> future) throws Exception {

//		Router router = Router.router(vertx);
//		router.route().handler(BodyHandler.create());
//		Set<String> allowedHeaders = setAllowedHeaders();
//		Set<HttpMethod> allowedMethods = setAllowedMethods();
//		//router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
//		
//		//config
////		router.get("/rest/reload/config").handler(CommonServiceHandler::handleReloadConfig);
//
//		
//		router.get("/test").handler(test -> {
//			System.out.println("aaaa");
//		}
//		);
//		
//		//collect start
////		router.get("/agent/https/start").handler(HttpsInputHandler::startserver);
//
//		// Serve the static resources
//		router.route().handler(StaticHandler.create());
//
//		vertx.createHttpServer().requestHandler(router::accept).listen(AgentConfig.restPort);
		
		
		
		
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());

		router.route()
				.handler(CorsHandler.create("*").allowedHeaders(setAllowedHeaders()).allowedMethods(setAllowedMethods()));

		router.post("/test").handler(event -> {
			try {
				System.out.println("aaaa");
			}catch(Exception e) {
				
			}
		});
		
		router.post("/test2").handler(event -> {
			event.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello!</h1></body></html>");
		});

		
		// Serve the static resources
		router.route().handler(StaticHandler.create());

		vertx.createHttpServer()
				.requestHandler(router::accept).listen(AgentConfig.restPort, result -> {
					if (result.succeeded()) {
						future.complete();
					} else {
						future.fail(result.cause());
					}
				});
		
		
		
	}

	private Set<String> setAllowedHeaders() {
		Set<String> allowedHeaders = new HashSet<>();
		allowedHeaders.add("x-requested-with");
		allowedHeaders.add("Access-Control-Allow-Origin");
		allowedHeaders.add("origin");
		allowedHeaders.add("Content-Type");
		allowedHeaders.add("accept");
		allowedHeaders.add("X-PINGARUNER");

		return allowedHeaders;
	}

	private Set<HttpMethod> setAllowedMethods() {
		Set<HttpMethod> allowedMethods = new HashSet<>();
		allowedMethods.add(HttpMethod.GET);
		allowedMethods.add(HttpMethod.POST);
		allowedMethods.add(HttpMethod.OPTIONS);
		/*
		 * these methods aren't necessary for this sample, but you may need them for
		 * your projects
		 */
		allowedMethods.add(HttpMethod.DELETE);
		allowedMethods.add(HttpMethod.PATCH);
		allowedMethods.add(HttpMethod.PUT);
		return allowedMethods;
	}
}