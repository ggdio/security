package br.com.ggdio.specs.application.exception;

import br.com.ggdio.specs.application.model.ErrorDetail;

/**
 * Exception for application layer
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 30 Jul 2018
 * @since 1.0.0-RELEASE
 */
public class ApplicationException extends RuntimeException implements ErrorDetail {

	private static final long serialVersionUID = -6517344987515177713L;
	
	private final String code;
	private final int httpStatus;
	
	public ApplicationException(String code, String message) {
		this(code, message, null);
	}
	
	public ApplicationException(String code, String message, Throwable cause) {
		this(code, message, 500, cause);
	}
	
	public ApplicationException(String code, String message, int httpStatus, Throwable cause) {
		super(message, cause);
		
		this.code = code;
		this.httpStatus = httpStatus;
	}
	
	@Override
	public String getCode() {
		return this.code;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
	@Override
	public int getHTTPStatus() {
		return httpStatus;
	}

}