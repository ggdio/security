package br.com.ggdio.security.domain.model;

/**
 * Authorization Types
 * 
 * @author Guilherme Dio
 *
 */
public enum AuthorizationType {

	OAUTH("bearer"),
	
	BASIC("basic"),
	
	DIGEST("digest")
	
	;
	
	private final String scheme;
	
	private AuthorizationType(final String scheme) {
		this.scheme = scheme;
	}
	
	public String getScheme() {
		return scheme;
	}
	
	public static AuthorizationType fromString(String value) {
		for (AuthorizationType type : AuthorizationType.values()) {
			if(type.getScheme().equalsIgnoreCase(value)) return type;
		}
		
		return null;
	}
	
}