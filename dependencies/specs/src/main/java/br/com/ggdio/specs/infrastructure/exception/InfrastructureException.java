package br.com.ggdio.specs.infrastructure.exception;

/**
 * Exception for Infrastructure Layer
 * 
 * @author Guilherme Dio
 *
 */
public class InfrastructureException extends RuntimeException {

	private static final long serialVersionUID = 1369105904417510055L;
	
	private final String errorCode;
	
	private static final String UNKNOWN_CODE = "UNKNOWN";

	public InfrastructureException(String msg) {
		this(UNKNOWN_CODE, msg, null);
	}
	
	public InfrastructureException(Throwable cause) {
		this(UNKNOWN_CODE, null, cause);
	}

	public InfrastructureException(String msg, Throwable cause) {
		this(UNKNOWN_CODE, msg, cause);
	}
	
	public InfrastructureException(String errorCode, String msg, Throwable cause) {
		super(msg, cause);
		
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

}