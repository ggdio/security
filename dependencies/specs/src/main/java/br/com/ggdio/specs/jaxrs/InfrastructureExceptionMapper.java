package br.com.ggdio.specs.jaxrs;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.ggdio.specs.application.view.ErrorView;
import br.com.ggdio.specs.infrastructure.exception.InfrastructureException;

/**
 * A model provider for not found exception at application layer
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 21 Aug 2018
 * @since 1.0.0-RELEASE
 */
@Provider
public class InfrastructureExceptionMapper implements ExceptionMapper<InfrastructureException> {

	@Override
	public Response toResponse(InfrastructureException e) {
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity(new ErrorView(e.getErrorCode(), e.getMessage(), e.getStackTrace()))
				.build();
	}
	
}
