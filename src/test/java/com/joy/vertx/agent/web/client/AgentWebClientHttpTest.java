package com.joy.vertx.agent.web.client;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentWebClientHttpTest implements AgentWebClientTest {
	private static final Logger logger = LoggerFactory.getLogger(AgentWebClientHttpTest.class);

	@Override
	public URLConnection connect(String hostName, int port) {

		HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection) new URL("http://" + hostName + ":" + port).openConnection();

			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");

			// give it 3 seconds to respond
			conn.setReadTimeout(3 * 1000);

		} catch (Exception ex) {
			logger.error("obj.openConnection() Error : ", ex);
		}
		return conn;
	}

	@Override
	public void disconnect(URLConnection conn) {
		try {
			if (conn != null) {
				logger.debug("conn.disconnect()");
				((HttpURLConnection) conn).disconnect();
			}
		} catch (Exception ex) {
			logger.error("conn.disconnect() Error : ", ex);

		} finally {
			conn = null;
		}
	}

	public void send(URLConnection conn, RequestMethod requestMethod, String requestBody) {

		try {
			switch (requestMethod) {
			case GET:

				((HttpURLConnection) conn).setRequestMethod(RequestMethod.GET.toString());

				break;

			case POST:

				conn.setDoOutput(true);
				((HttpURLConnection) conn).setRequestMethod(RequestMethod.POST.toString());

				if (requestBody != null && !requestBody.isEmpty()) {
					try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
						wr.write(requestBody);
						wr.flush();
					}
				}

				break;
			}

			if (conn != null) {
				int responseCode = ((HttpURLConnection) conn).getResponseCode();
				if (responseCode != 200) {
					logger.debug("Response Code : " + responseCode);
				}
			}

			print_content(conn);

		} catch (Exception ex) {
			logger.error("send() Error : ", ex);
		}
	}

}
