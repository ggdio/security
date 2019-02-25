package br.com.ggdio.specs.template.exception;

/**
 * Exception for renderer errors
 * 
 * @author Marcus Zanini
 *
 */
public class RendererException extends RuntimeException {

	private static final long serialVersionUID = 1737194585176359265L;

	public RendererException(String message, Throwable cause) {
		super(message, cause);
	}

}
