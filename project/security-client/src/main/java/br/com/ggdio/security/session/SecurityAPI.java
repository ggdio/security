package br.com.ggdio.security.session;

/**
 * API for consuming security microservice
 * @author Guilherme Dio
 *
 */
public class SecurityAPI {
	
	private final HttpClient microservice;

	public SecurityAPI(String endpoint) {
		microservice = new HttpClient(endpoint);
	}
	
	public Role getRole(String key) {
		return microservice.getRole(key);
	}
	
}