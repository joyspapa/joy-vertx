package com.joy.vertx.agent.web.client;

import java.net.URLConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain {
	private static final Logger logger = LoggerFactory.getLogger(TestMain.class);
	
	private long startTime;
	
	AgentWebClientTest webClient;
	URLConnection conn;
	
	/*
	 * HTTP Test
	 */
	@Test
	public void sendHttpGET() {
		webClient = new AgentWebClientHttpTest();
		sendGET("localhost", 8092);
	}

	@Test
	public void sendHttpPOST() {
		webClient = new AgentWebClientHttpTest();
		sendPOST("localhost", 8092, "{\"data\" : \"this is tesing(한글)!\"}");
	}

	/*
	 * HTTPS Test
	 */
	@Test
	public void sendHttpsGET() {
		webClient = new AgentWebClientHttpsTest();
		sendGET("localhost", 4443);
	}

	@Test
	public void sendHttpsPOST() {
		webClient = new AgentWebClientHttpsTest();
		sendPOST("localhost", 4443, "{\"data\" : \"this is tesing(한글)!\"}");
	}
	
	/*
	 * Common
	 */
	@Before
	public void setUp() {
		startTime = System.currentTimeMillis();
	}

	@After
	public void tearDown() {
		webClient.disconnect(conn);
		logger.info("elapsedTime : " + (System.currentTimeMillis() - startTime) + " ms");
	}
	
	private void sendPOST(String hostName, int port, String requestBody) {
		conn = webClient.connect(hostName, port);
		webClient.send(conn, RequestMethod.POST, requestBody);
	}
	
	private void sendGET(String hostName, int port) {
		conn = webClient.connect(hostName, port);
		webClient.send(conn, RequestMethod.GET);
	}
}
