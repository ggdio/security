package br.com.ggdio.specs.event.exception;

/**
 * Exception wrapper for mq providers errors
 * 
 * @author Guilherme Dio
 *
 */
public class EventConnectionException extends RuntimeException {

	private static final long serialVersionUID = -3194840151274155153L;
	
	public EventConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

}