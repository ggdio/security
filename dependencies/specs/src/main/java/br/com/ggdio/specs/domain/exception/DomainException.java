package br.com.ggdio.specs.domain.exception;

/**
 * Exception for Domain Layer
 * 
 * @author Guilherme Dio
 *
 */
public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 1369105904417510055L;
	
	private final String errorCode;
	
	private static final String UNKNOWN_CODE = "UNKNOWN";

	public DomainException(String msg) {
		this(UNKNOWN_CODE, msg, null);
	}
	
	public DomainException(Throwable cause) {
		this(UNKNOWN_CODE, null, cause);
	}

	public DomainException(String msg, Throwable cause) {
		this(UNKNOWN_CODE, msg, cause);
	}
	
	public DomainException(String errorCode, String msg, Throwable cause) {
		super(msg, cause);
		
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

}