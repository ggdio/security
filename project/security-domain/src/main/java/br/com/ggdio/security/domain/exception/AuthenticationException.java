package br.com.ggdio.security.domain.exception;

import br.com.ggdio.specs.domain.exception.DomainException;

/**
 * Exception for Authentication
 * @author Guilherme Dio
 *
 */
public class AuthenticationException extends DomainException {
	
	private static final long serialVersionUID = -5784235883209475249L;
	
	public AuthenticationException() {
		this("", null);
	}

	public AuthenticationException(String message) {
		this(message, null);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AuthenticationException(Error error, Object...args) {
		super(error.getCode(), error.getMessage(args), null);
	}
	
	public AuthenticationException(Error error, Throwable cause, Object...args) {
		super(error.getCode(), error.getMessage(args), cause);
	}

}