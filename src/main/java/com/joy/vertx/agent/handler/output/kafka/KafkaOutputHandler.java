package com.joy.vertx.agent.handler.output.kafka;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.joy.vertx.agent.config.AgentConfig;
import com.joy.vertx.agent.handler.output.OutputAgentHandler;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class KafkaOutputHandler implements OutputAgentHandler{
	public static void collectingLog(RoutingContext routingContext){
		
		String jsondata = routingContext.request().getParam("data");
		System.out.println(routingContext.getBodyAsString());
		String topicName = routingContext.request().getParam("topic");

		HttpServerResponse httpServerResponse = routingContext.response();
		httpServerResponse.setChunked(true);
		MultiMap headers = routingContext.request().headers();

		// Set properties used to configure the producer
		Properties properties = new Properties();
		// Set the brokers (bootstrap servers)
		properties.setProperty("bootstrap.servers", AgentConfig.bootstrapServers);
		// Set how to serialize key/value pairs
		properties.setProperty("key.serializer", AgentConfig.keySerializer);
		properties.setProperty("value.serializer", AgentConfig.valueSerializer);

		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

		try {
			producer.send(new ProducerRecord<String, String>(topicName, jsondata)).get();
		} catch (Exception ex) {
			System.out.print(ex.getMessage());
			//throw new IOException(ex.toString());
		} finally {
			producer.close();
		}
		try {
			httpServerResponse.write(jsondata);
		} catch (Exception e) {

		}

		httpServerResponse.putHeader("Content-Type", "application/text").end("Success");

	}
}
