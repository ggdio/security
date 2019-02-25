package br.com.ggdio.client.common.converter;

/**
 * Exception type for Converter
 * @author Guilherme Dio
 *
 */
public class ConverterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConverterException() {
		
	}

	public ConverterException(String message) {
		super(message);
	}
	
	public ConverterException(String message, Throwable cause) {
		super(message, cause);
	}
	
}