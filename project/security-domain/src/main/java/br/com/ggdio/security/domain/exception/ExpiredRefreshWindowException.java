package br.com.ggdio.security.domain.exception;

import br.com.ggdio.specs.domain.exception.DomainException;

/**
 * Exception for expired refresh tokens
 * 
 * @author Guilherme Dio
 *
 */
public class ExpiredRefreshWindowException extends DomainException {
	
	private static final long serialVersionUID = -4504956206123083366L;

	public ExpiredRefreshWindowException(String refreshToken) {
		super(Error.EXPIRED_REFRESH_WINDOW.getCode(), Error.EXPIRED_REFRESH_WINDOW.getMessage(refreshToken), null);
	}

}