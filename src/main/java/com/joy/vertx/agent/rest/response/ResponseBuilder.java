package com.joy.vertx.agent.rest.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class ResponseBuilder {
	private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

	public static void sendError(String message, HttpServerResponse response) {
		sendError(message, response, 200);
	}

	public static void sendError(String message, HttpServerResponse response, int statusCode) {
		sendError(message, response, null, statusCode);
	}

	public static void sendError(String message, HttpServerResponse response, Object data) {
		sendError(message, response, null, 200, data);
	}
	
	public static void sendError(String message, HttpServerResponse response, Throwable _th) {
		sendError(message, response, _th, 200);
	}

	public static void sendError(String message, HttpServerResponse response, Throwable _th, int statusCode) {
		sendError(message, response, _th, 200, null);
	}
	
	public static void sendError(String message, HttpServerResponse response, Throwable _th, int statusCode, Object data) {
		JsonObject responseObject = new JsonObject();
		responseObject.put("success", false).put("message", message);

		if (_th != null) {
			responseObject.put("errorcode", (_th.getCause() != null) ? _th.getCause().toString() : "E")
					.put("errormessage", _th.toString());
		}

		if (data != null) {
			if (data instanceof JsonObject || data instanceof JsonArray) {
				responseObject.put("data", data);
			} else {

				responseObject.put("data", new JsonObject().mapFrom(data));
			}
		}
		
		logger.warn("sendError. response: \n" + responseObject.toString());
		
		response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json").setStatusCode(statusCode).setChunked(true)
				.end(responseObject.toString(), "UTF-8");
	}

	public static void sendSuccess(String message, HttpServerResponse response) {
		sendSuccess(message, response, null);
	}

	public static void sendSuccess(String message, HttpServerResponse response, Object data) {
		JsonObject responseObject = new JsonObject();
		if (data instanceof JsonObject || data instanceof JsonArray) {
			responseObject.put("success", true).put("message", message).put("data", data);
		} else {

			responseObject.put("success", true).put("message", message).put("data", new JsonObject().mapFrom(data));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("sendSuccess. response: \n" + responseObject/*.encodePrettily()*/);
		}
		
		sendSuccess(response, responseObject);
		
	}

	public static void sendSuccess(String message, HttpServerResponse response, String data) {
		JsonObject responseObject = new JsonObject();
		responseObject.put("success", true).put("message", message);

		if (data == null || data.isEmpty()) {
			responseObject.put("data", data);
		} else {
			responseObject.put("data", new JsonObject(data));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("sendSuccess. response: \n" + responseObject/*.encodePrettily()*/);
		}
		sendSuccess(response, responseObject);
	}

	public static void sendSuccess(HttpServerResponse response, JsonObject responseObject) {
		response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json").setStatusCode(200).setChunked(true)
				.end(responseObject.toString(), "UTF-8");
	}
}