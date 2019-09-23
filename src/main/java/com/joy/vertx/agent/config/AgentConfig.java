package com.joy.vertx.agent.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.obzen.logplanet.rest.repository.ClusterInfoRepository;

public class AgentConfig {

	private static final Logger logger = LoggerFactory.getLogger(AgentConfig.class);

	private static final String propertyFileName = "config.properties";

	public static int restPort = 8080;
	public static int httpsPort = 4443;
	public static String bootstrapServers = "192.168.10.82:9092,192.168.10.83:9092,192.168.10.84:9092";
	public static String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
	public static String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
	public static String publickeypath = "D:\\git\\log-planet\\Agent\\logplanet-agent\\src\\main\\resources\\server-keystore.jks";
	public static String publickeypwd = "wibble";
	private static Properties prop;

	

	private static void loadConfig() throws Exception {

		prop = new Properties();

		try (InputStream is = AgentConfig.class.getClassLoader().getResourceAsStream(propertyFileName)) {

			prop.load(is);

			restPort = Integer.parseInt(AgentConfig.getProperties().getProperty("agent.rest.port"));
			httpsPort = Integer.parseInt(AgentConfig.getProperties().getProperty("agent.https.port"));
			bootstrapServers = AgentConfig.getProperties().getProperty("bootstrap.servers");
			keySerializer = AgentConfig.getProperties().getProperty("key.serializer");
			valueSerializer = AgentConfig.getProperties().getProperty("value.serializer");
			publickeypath = AgentConfig.getProperties().getProperty("agent.input.https.public.key.path");
			publickeypwd = AgentConfig.getProperties().getProperty("agent.input.https.public.key.pwd");
			logger.info("It has done loading the " + propertyFileName);

		} catch (IOException e) {
			logger.error("Error in initializing rest properties", e);
			throw e;
		}

	}

	private static Properties getProperties() throws Exception {
		if (prop == null) {
			loadConfig();
		}

		return prop;
	}

	public static void reloadConfig() throws Exception {
		loadConfig();
	}

	public static String getValue(String key) {
		try {
			return getProperties().getProperty(key);
		} catch(Exception ex) {
			return null;
		}
		
	}

	public static void setProperties(String key, String value) {
		try {
			getProperties().setProperty(key, value);
		} catch(Exception ex) {
			logger.error("Error in initializing rest setProperties", ex);
		}
	}

}