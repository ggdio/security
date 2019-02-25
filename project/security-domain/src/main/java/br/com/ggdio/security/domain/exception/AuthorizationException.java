package br.com.ggdio.security.domain.exception;

import br.com.ggdio.specs.domain.exception.DomainException;

/**
 * Exception for Authentication
 * @author Guilherme Dio
 *
 */
public class AuthorizationException extends DomainException {
	
	private static final long serialVersionUID = 1184368046039441806L;

	public AuthorizationException(String message) {
		this(message, null);
	}

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AuthorizationException(Error error, Object...args) {
		super(error.getCode(), error.getMessage(args), null);
	}

}