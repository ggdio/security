package br.com.ggdio.specs.application.exception;

import javax.ws.rs.core.Response.Status;

import br.com.ggdio.specs.application.exception.ApplicationException;
import br.com.ggdio.specs.application.model.ErrorDetail;

import javax.ws.rs.core.UriInfo;

/**
 * Detailed application exception with URI INFO
 * 
 * @author Guilherme Dio
 *
 */
public class DetailedApplicationException extends ApplicationException {
	
	private static final long serialVersionUID = 3096329574122878148L;
	
	private final UriInfo uri;
	private final Object[] args;
	
	public DetailedApplicationException(ErrorDetail detail, UriInfo uri, Object...args) {
		this(detail, detail.getHTTPStatus(), uri, args);
	}
	
	public DetailedApplicationException(ErrorDetail detail, Status status, UriInfo uri, Object...args) {
		this(detail, status.getStatusCode(), uri, args);
	}
	
	public DetailedApplicationException(ErrorDetail detail, int status, UriInfo uri, Object...args) {
		 super(detail.getCode(), detail.getMessage(), status, null);
		 
		 this.uri = uri;
		 this.args = args;
	}
	
	public UriInfo getUri() {
		return uri;
	}
	
	public Object[] getArgs() {
		return args;
	}

}