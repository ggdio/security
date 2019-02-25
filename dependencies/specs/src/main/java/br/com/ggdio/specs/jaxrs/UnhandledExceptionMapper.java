package br.com.ggdio.specs.jaxrs;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.ggdio.specs.application.view.ErrorView;

/**
 * A model provider for unhandled exception at application layer
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 31 Jul 2018
 * @since 1.0.0-RELEASE
 */
@Provider
public class UnhandledExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable e) {
		final ErrorView view;
		final int status;
		
		if(e instanceof WebApplicationException) {
			Response response = ((WebApplicationException) e).getResponse();
			
			status = response.getStatus();
			view = new ErrorView(e.getClass().getSimpleName(), e.getMessage(), new String[0]);
			
		} else {
			status = Status.INTERNAL_SERVER_ERROR.getStatusCode();
			view = new ErrorView("Unexpected", "Unexpected error: " + e.getMessage(),e.getStackTrace());
			
		}
		
		view.status(status);
		
		return Response
				.status(status)
				.entity(view)
				.build();
	}
	
}
