package com.joy.vertx.agent.handler.input.https;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joy.vertx.agent.config.AgentConfig;
import com.joy.vertx.agent.handler.input.InputAgentHandler;
import com.joy.vertx.agent.handler.output.kafka.KafkaOutputHandler;
import com.joy.vertx.agent.rest.response.ResponseBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.JksOptions;
//import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 * reviewed by: Giacomo Venturini mail: giacomo.venturini3@gmail.com"
 */
public class HttpsInputHandler extends AbstractVerticle implements InputAgentHandler {

	private static final Logger logger = LoggerFactory.getLogger(HttpsInputHandler.class);
	
	public static void startserver(RoutingContext routingContext) {
		HttpServerResponse httpServerResponse = routingContext.response();
		httpServerResponse.setChunked(true);
		httpServerResponse.putHeader("Content-Type", "application/text").end("Success");
		Launcher.main(new String[] { "run", HttpsInputHandler.class.getName() });
		
	}
	

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Launcher.main(new String[] { "run", HttpsInputHandler.class.getName() });
	}

	@Override
	public void start() throws Exception {

		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());

		router.route()
				.handler(CorsHandler.create("*").allowedHeaders(setAllowedHeaders()).allowedMethods(setAllowMethods()));

		router.get("/test2").handler(testtest -> {System.out.println("aaaasdfaasdfaasdasfas");
		ResponseBuilder.sendSuccess(
                "hello"
                , testtest.response()
                , "{\"test\":\"test\"}"
        );});
		
		router.post("/agent").handler(event -> {
			try {
				KafkaOutputHandler.collectingLog(event);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		// Serve the static resources
		router.route().handler(StaticHandler.create());

		vertx.createHttpServer(new HttpServerOptions().setSsl(true)
				.setKeyStoreOptions(new JksOptions().setPath("server-keystore.jks").setPassword("wibble")))
				.requestHandler(router::accept).listen(AgentConfig.httpsPort);
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

	private Set<HttpMethod> setAllowMethods() {
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

	public String startDeployment() {
		return "TO-DO";
	}

	public String stopDeployment() {
		return "TO-DO";
	}
}