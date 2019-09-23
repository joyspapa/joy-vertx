package com.joy.vertx.agent.web.client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentWebClientHttpsTest implements AgentWebClientTest {
	private static final Logger logger = LoggerFactory.getLogger(AgentWebClientHttpsTest.class);

	@Override
	public URLConnection connect(String hostName, int port) {
		getHostnameVerifier();

		HttpsURLConnection conn = null;
		
		try {
			conn = (HttpsURLConnection) new URL("https://" + hostName + ":" + port).openConnection();

			conn.setHostnameVerifier(getHostnameVerifier());

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
				((HttpsURLConnection)conn).disconnect();
			}
		} catch (Exception ex) {
			logger.error("conn.disconnect() Error : ", ex);

		} finally {
			conn = null;
		}
	}

	public void send(URLConnection conn, RequestMethod requestMethod) {
		send(conn, requestMethod, null);
	}

	public void send(URLConnection conn, RequestMethod requestMethod, String requestBody) {

		try {

			switch (requestMethod) {
			case GET:
				
				((HttpsURLConnection)conn).setRequestMethod(RequestMethod.GET.toString());
				
				break;

			case POST:

				conn.setDoOutput(true);
				((HttpsURLConnection)conn).setRequestMethod(RequestMethod.POST.toString());

				if (requestBody != null && !requestBody.isEmpty()) {
					try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
						wr.write(requestBody);
						wr.flush();
					}
				}

				break;
			}

			if (conn != null) {
				logger.debug("Response Code : " + ((HttpsURLConnection)conn).getResponseCode());
			}
			
			print_https_cert(conn);

			print_content(conn);

		} catch (Exception ex) {
			logger.error("send() Error : ", ex);
		}
	}

	private TrustManager[] getTrustAllCerts() {

		return new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
				/* No hacemos nada */ }

			@Override
			public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
				/* No hacemos nada */ }

		} };
	}

	private HostnameVerifier getHostnameVerifier() {
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, getTrustAllCerts(), new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception ex) {
			logger.error("getHostnameVerifier() Error : ", ex);
		}

		return new HostnameVerifier() {
			@Override
			public boolean verify(String paramString, SSLSession paramSSLSession) {
				return true;
			}
		};
	}
	
	private void print_https_cert(URLConnection conn) {

		if (conn != null) {
			try {
				logger.debug("Response Code : " + ((HttpsURLConnection)conn).getResponseCode());
				logger.debug("Cipher Suite : " + ((HttpsURLConnection)conn).getCipherSuite());
				logger.debug("\n");

				Certificate[] certs = ((HttpsURLConnection)conn).getServerCertificates();
				for (Certificate cert : certs) {
					logger.debug("Cert Type : " + cert.getType());
					logger.debug("Cert Hash Code : " + cert.hashCode());
					logger.debug("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
					logger.debug("Cert Public Key Format : " + cert.getPublicKey().getFormat());
					logger.debug("\n");
				}

			} catch (SSLPeerUnverifiedException ex) {
				logger.error("print_https_cert() Error : ", ex);
			} catch (IOException ex) {
				logger.error("print_https_cert() Error : ", ex);
			}
		}
	}

}
