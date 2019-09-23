package com.joy.vertx.agent.web.server;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class AgentWebServerHttpsVerticleTest extends AbstractVerticle implements AgentWebServerTest {
	private static final Logger logger = LoggerFactory.getLogger(AgentWebServerHttpsVerticleTest.class);
	
	// https://namjackson.tistory.com/24
	private String keyPath;
	private String certPath;
	private String keyStorePath;
	private String keyStorePwd;
	
	private int port = 4443;
	
	public static void main(String[] args) {
		Launcher.main(new String[] { "run", AgentWebServerHttpsVerticleTest.class.getName() });
	}

	@Override
	public void setVariable() {
		
		if(port == 0) {
			throw new IllegalArgumentException("setVariable() check the port : " + port);
		}
		
		String os = System.getProperty("os.name");
		logger.debug("OS name : " + os);
		
		if(os.startsWith("Windows")) {
			keyPath = "C:\\openssl\\ssl\\bin\\server.key";
			certPath = "C:\\openssl\\ssl\\bin\\server.crt";
			keyStorePath = "server-keystore.jks";
			keyStorePwd = "wibble";
		} else {
			keyPath = "/working/temp/private.key";
			certPath = "/working/temp/private.crt";
			keyStorePath = "server-keystore.jks";
			keyStorePwd = "wibble";
		}
	}
	
	@Override
	public void start() throws Exception {
		setVariable();
		
		HttpServer server = vertx.createHttpServer(getHttpServerOptions());
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		allowedCORS(router);
		
		// Common service
		routeService(router);
				
		server.requestHandler(router::accept).listen(port);
		
		logger.info("... Server has been started with this port : " + port);
	}

	private HttpServerOptions getHttpServerOptions() {
		HttpServerOptions httpOpts = new HttpServerOptions();
		//certPath = ".jks";
		logger.debug("certPath : " + certPath);
		keyStorePath = null;
		keyStorePwd = null;
		
		if (keyStorePath != null && keyStorePwd != null) {
			logger.debug("is setPemKeyCertOptions : false");
			httpOpts.setKeyStoreOptions(new JksOptions().setPath(keyStorePath).setPassword(keyStorePwd));
			httpOpts.setSsl(true);
			
		} else if (keyPath != null && certPath != null) {
			logger.debug("is setPemKeyCertOptions : true");
			httpOpts.setPemKeyCertOptions(new PemKeyCertOptions().setCertPath(certPath).setKeyPath(keyPath));
			httpOpts.setSsl(true);
			
		} else {

			throw new IllegalArgumentException("Unknown certPath : " + certPath);
			
		}

		return httpOpts;
	}
	
	private void allowedCORS(Router router) {
		Set<String> allowedHeaders = new HashSet<>();
	    allowedHeaders.add("x-requested-with");
	    allowedHeaders.add("Access-Control-Allow-Origin");
	    allowedHeaders.add("origin");
	    allowedHeaders.add("Content-Type");
	    allowedHeaders.add("accept");
	    allowedHeaders.add("X-PINGARUNER");

	    Set<HttpMethod> allowedMethods = new HashSet<>();
	    allowedMethods.add(HttpMethod.GET);
	    allowedMethods.add(HttpMethod.POST);
	    allowedMethods.add(HttpMethod.OPTIONS);
	    /*
	     * these methods aren't necessary for this sample, 
	     * but you may need them for your projects
	     */
	    allowedMethods.add(HttpMethod.DELETE);
	    allowedMethods.add(HttpMethod.PATCH);
	    allowedMethods.add(HttpMethod.PUT);

	    router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
	}
}
