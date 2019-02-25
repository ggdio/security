package br.com.ggdio.security.domain.exception;

import br.com.ggdio.specs.domain.exception.DomainException;

/**
 * Exception for expired session tokens
 * 
 * @author Guilherme Dio
 *
 */
public class InvalidTokenException extends DomainException {
	
	private static final long serialVersionUID = 6784076316426455187L;

	public InvalidTokenException(String token) {
		super(Error.INVALID_TOKEN.getCode(), Error.INVALID_TOKEN.getMessage(token), null);
	}

}