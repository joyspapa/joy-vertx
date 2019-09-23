package com.joy.vertx.agent.web.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

enum RequestMethod {
	GET, POST;
}

public interface AgentWebClientTest {
	static final Logger logger = LoggerFactory.getLogger(AgentWebClientTest.class);

	URLConnection connect(String hostName, int port);

	void disconnect(URLConnection conn);

	default void send(URLConnection conn, RequestMethod requestMethod) {
		send(conn, requestMethod, null);
	}

	void send(URLConnection conn, RequestMethod requestMethod, String requestBody);

	default void print_content(URLConnection conn) {
		if (conn != null) {

			try {

				try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {

					logger.debug("****** Content of the URL ********");

					String input;

					while ((input = br.readLine()) != null) {
						logger.debug(input);
					}

				} catch (Exception ex) {
					logger.error("print_content() Error : ", ex);
				}
			} catch (Exception ex) {
				logger.error("print_content() Response Code Error : ", ex);
			}
		}

	}
}
