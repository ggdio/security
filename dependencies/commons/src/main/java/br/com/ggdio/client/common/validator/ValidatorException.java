package br.com.ggdio.client.common.validator;

/**
 * Exception type for validators
 * @author Guilherme Dio
 *
 */
public class ValidatorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidatorException() {
		
	}

	public ValidatorException(String message) {
		super(message);
	}
	
	public ValidatorException(String message, Throwable cause) {
		super(message, cause);
	}
	
}