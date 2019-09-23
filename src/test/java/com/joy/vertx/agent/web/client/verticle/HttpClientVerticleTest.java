package com.joy.vertx.agent.web.client.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class HttpClientVerticleTest extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientVerticleTest.class);
	
	private int count = 2;
	
	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Launcher.main(new String[] { "run", HttpClientVerticleTest.class.getName() });
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);

		HttpRequest<Buffer> request = client.put(8092, "ecube89", "/");
		
		JsonObject user = null;

		for(int i = 0 ; i < count ; i++ ) {
			user = new JsonObject().put("firstName", "Dale" + i).put("lastName", "Cooper").put("male", true).put("count", "num_"+i);
		
			request.sendJson(user, ar -> {
				if (ar.succeeded()) {
					HttpResponse<Buffer> response = ar.result();
					System.out.println("Got HTTP response with status " + response.statusCode());
				} else {
					ar.cause().printStackTrace();
				}
			});
		}
		
		logger.debug("전송완료");
		//client.close();
		//vertx.close();
		
	}
	
	
}
