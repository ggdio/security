package br.com.ggdio.security.session;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

import br.com.ggdio.json.JSONUtils;
import br.com.ggdio.security.client.MapUtils;

class HttpClient {

	private final String endpoint;
	private final Client client;

	HttpClient(String endpoint) {
		if (endpoint == null || endpoint.isEmpty())
			throw new IllegalArgumentException("The specified endpoint is null or empty");

		if (!endpoint.endsWith("/"))
			endpoint += "/";
		if (!endpoint.startsWith("http://") && !endpoint.startsWith("https://"))
			endpoint = "http://" + endpoint;

		this.endpoint = endpoint;
		this.client = initClient();
	}

	void destroy(String token) {
		client.resource(url("auth")).accept("application/json").header("Authorization", "Bearer " + token)
				.delete(ClientResponse.class);
	}

	boolean isValid(String token) {
		return validate(token) != null;
	}

	@SuppressWarnings("unchecked")
	Session validate(String token) {
		ClientResponse response = client.resource(url("auth")).accept("application/json")
				.header("Authorization", "Bearer " + token).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			return null;
		}

		final String json = response.getEntity(String.class);
		Map<String, Object> data = JSONUtils.fromJSON(HashMap.class, json);
		Session session = new Session();
		session.setToken(MapUtils.getString(data, "access_token"));
		session.setTokenType(MapUtils.getString(data, "token_type"));
		session.setUserId(MapUtils.getString(data, "user_id"));
		session.setExpiresIn(MapUtils.getLong(data, "expires_in"));

		Map<String, Object> scope = MapUtils.getMap(data, "scope");

		Object appsRef = scope.get("apps");
		if (appsRef != null) {
			session.addApps((List<String>) appsRef);
		}

		Object rolesRef = scope.get("roles");
		if (rolesRef != null) {
			session.addRoles((List<String>) rolesRef);
		}

		Map<String, Object> actionsRef = MapUtils.getMap(scope, "actions");
		if (actionsRef != null && !actionsRef.isEmpty()) {
			for (Map.Entry<String, Object> entry : actionsRef.entrySet()) {
				Object value = entry.getValue();
				try {
					boolean grant = (boolean) value;
					if (grant) {
						session.addAction(entry.getKey());
					}

				} catch (Exception e) {
				}
			}

		}

		return session;
	}

	Role getRole(String key) {
		ClientResponse response = client.resource(url("roles", key)).accept("application/json")
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			return null;
		}

		final String json = response.getEntity(String.class);
		return JSONUtils.fromJSON(Role.class, json);
	}

	private String url(String... args) {
		String url = endpoint;
		if (args == null || args.length == 0)
			return url;

		for (int i = 0; i < args.length - 1; i++) {
			String arg = args[i];
			url = url.concat(arg).concat("/");
		}

		return url.concat(args[args.length - 1]);
	}

	private Client initClient() {
		URLConnectionClientHandler clientHandler = getClientHandler();
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put("jersey.config.client.connectTimeout", 20000);
		config.getProperties().put("jersey.config.client.readTimeout", 20000);
		config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
				new com.sun.jersey.client.urlconnection.HTTPSProperties(getHostnameVerifier(), getSSLContext()));
		Client client = new Client(clientHandler, config);

		return client;
	}

	private URLConnectionClientHandler getClientHandler() {
		return new URLConnectionClientHandler(new HttpURLConnectionFactory() {
			@Override
			public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
				return (HttpURLConnection) url.openConnection();
			}
		});
	}

	private HostnameVerifier getHostnameVerifier() {
		return new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
				HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();

				return hv.verify(hostname, sslSession);
			}
		};
	}

	private SSLContext getSSLContext() {
		SSLContext ctx = null;
		try {
			javax.net.ssl.TrustManager x509 = new EasyX509TrustManager(null);
			ctx = SSLContext.getInstance("SSL");
			ctx.init(null, new javax.net.ssl.TrustManager[] { x509 }, null);
		} catch (java.security.GeneralSecurityException ex) {
		}
		return ctx;
	}

}