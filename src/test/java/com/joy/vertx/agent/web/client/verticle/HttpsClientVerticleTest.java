package com.joy.vertx.agent.web.client.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
//import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.WebClientOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class HttpsClientVerticleTest extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(HttpsClientVerticleTest.class);

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Launcher.main(new String[] { "run", HttpsClientVerticleTest.class.getName() });
	}

	@Override
	public void start() throws Exception {
		WebClientOptions options = new WebClientOptions();
		options.setUseAlpn(true);
		options.setSsl(true);
		options.setTrustAll(true);
		options.setVerifyHost(false);

		// Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.
		WebClient client = null;
		try {
			client = WebClient.create(vertx, options);

			HttpRequest<Buffer> request = client.post(4443, "localhost", "/");
			//request = client.get(4443, "localhost", "/");

			JsonObject user = null;
			int count = 2;

			for (int i = 0; i < count; i++) {
				user = new JsonObject().put("firstName", "Dale" + i).put("lastName", "Cooper").put("male", true)
						.put("count", i);

				request.sendJson(user, responseHandler -> {
					if (responseHandler.succeeded()) {
						HttpResponse<Buffer> response = responseHandler.result();
						logger.debug("Got HTTP response with status : " + response.statusCode());
						logger.debug("Got data : " + response.bodyAsString());
					} else {
						logger.error("responseHandler Error : ", responseHandler.cause());
					}

				});
				logger.debug("sendJson()");
			}
			//Thread.sleep(5000); 2019-02-13 14:15:08,093
		} finally {
			logger.debug("close()");
			client.close();
			//vertx.close();
		}
	}
}