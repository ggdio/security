package br.com.ggdio.security.client;

import javax.ws.rs.core.Response.Status;

import br.com.ggdio.specs.application.model.ErrorDetail;

/**
 * Enumeration for error codes and descriptions
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 21 Jun 2018
 * @since 1.0.0-RELEASE
 */
public enum Error implements ErrorDetail {
	
	INVALID_CREDENTIALS("AUTH-001", "Invalid username and/or password!", Status.UNAUTHORIZED.getStatusCode()),
	ACCESS_FORBIDDEN("AUTH-002", "Access is forbidden!", Status.FORBIDDEN.getStatusCode()),
	INVALID_TOKEN("AUTH-003", "Invalid token!", Status.UNAUTHORIZED.getStatusCode()),
	;

    private final String code;
	private final String message;
	private final int httpCode;
	
	private Error(String code, String message, int httpCode) {
		this.code = code;
		this.message = message;
		this.httpCode = httpCode;
	}
	
	@Override
	public String getCode() {
		return code;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	public String getMessage(Object...args) {
		if(args == null || args.length == 0) return message;
		
		return String.format(message, args);
	}

	@Override
	public int getHTTPStatus() {
		return httpCode;
	}
	
}
