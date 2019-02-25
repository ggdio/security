package br.com.ggdio.security.application.rest.view.enumeration;

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
	
	INVALID_URI("RE-001", "The requested URI is invalid.", Status.BAD_REQUEST.getStatusCode()),
	
	ROLE_NOT_FOUND("AUTH-100", "Role '%s' not found.", Status.NOT_FOUND.getStatusCode()),
	
	INVALID_CREDENTIALS("AUTH-001", "Invalid username and/or password!", Status.UNAUTHORIZED.getStatusCode()),
	ACCESS_FORBIDDEN("AUTH-002", "Access is forbidden!", Status.FORBIDDEN.getStatusCode()),
	INVALID_TOKEN("AUTH-003", "Invalid token!", Status.UNAUTHORIZED.getStatusCode()),
		
	UNEXPECTED_EROR("UE-001", "Unexpected error: '%s'.", Status.INTERNAL_SERVER_ERROR.getStatusCode())
	;
	
	private final String code;
	private final String message;
	private final int httpStatus;
	
	private Error(String code, String message, int httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}
	
	@Override
	public String getCode() {
		return code;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	@Override
	public int getHTTPStatus() {
		return httpStatus;
	}
	
}