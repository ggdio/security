package br.com.ggdio.specs.application.model;

/**
 * Interface for error detailing
 *
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 30 Jul 2018
 * @since 1.0.0-RELEASE
 */
public interface ErrorDetail {

	public String getCode();
	
	public int getHTTPStatus();
	
	public String getMessage();
	
}