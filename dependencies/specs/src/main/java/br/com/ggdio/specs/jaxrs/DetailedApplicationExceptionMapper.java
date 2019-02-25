package br.com.ggdio.specs.jaxrs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.ggdio.specs.application.exception.DetailedApplicationException;
import br.com.ggdio.specs.application.view.ErrorView;

/**
 * A model provider for not found exception at application layer
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 31 Jul 2018
 * @since 1.0.0-RELEASE
 */
@Provider
public class DetailedApplicationExceptionMapper implements ExceptionMapper<DetailedApplicationException> {
	
	@Context 
	private HttpServletRequest request;

	@Override
	public Response toResponse(DetailedApplicationException e) {
		return Response
				.status(e.getHTTPStatus())
				.entity(ErrorView.from(e, e.getArgs())
							.path(e.getUri().getAbsolutePath().getPath())
							.method(request.getMethod()))
				.build();
	}
	
}
