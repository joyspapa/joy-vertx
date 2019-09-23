package com.joy.vertx.agent.web.client;

import java.net.URLConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMainForPerformance {
	private static final Logger logger = LoggerFactory.getLogger(TestMainForPerformance.class);

	private long startTime;
	
	private static AgentWebClientTest webClient;
	URLConnection conn;

	@Test
	public void testHttpGet() {
		webClient = new AgentWebClientHttpTest();
		
		//testHttpGet01(20000, 1000, 500, RequestMethod.GET);
		send("ecube89", 8092, 2, 0, 0, RequestMethod.GET);
	}

	@Test
	public void testHttpPOST() {
		webClient = new AgentWebClientHttpTest();
		
		send("ecube89", 8092, 2, 0, 0, RequestMethod.POST);
	}
	
	@Test
	public void testHttpsPOST() {
		webClient = new AgentWebClientHttpsTest();
		
		send("localhost", 4443, 1000, 0, 0, RequestMethod.POST);
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
	
	private String makeRequstBody(long index) {
		// TODO
		return "{\"message\" : \"num_" + index +"\"}";
	}
	
	private void send(String hostName, int port, long count, int chunkSize, int sleepTime, RequestMethod requestMethod) {
		for (long idx = 0; idx < count; idx++) {

			if(chunkSize > 0 && idx%chunkSize == 0) {
				if(0 < sleepTime) {
					try {
						Thread.sleep(sleepTime);
						logger.info("chunkSize : " + chunkSize + " , idx : " + idx + " , sleepTime : " + sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			switch (requestMethod) {
			case GET:
				sendGET(hostName, port);
				break;

			case POST:
				sendPOST(hostName, port, makeRequstBody(idx));
				break;
			}
			
		}
	}
	
	private void sendGET(String hostName, int port) {
		conn = webClient.connect(hostName, port);
		webClient.send(conn, RequestMethod.GET);
		webClient.disconnect(conn);
	}
	
	private void sendPOST(String hostName, int port, String requestBody) {
		conn = webClient.connect(hostName, port);
		webClient.send(conn, RequestMethod.POST, requestBody);
		webClient.disconnect(conn);
	}
}
