package br.com.ggdio.security.jaxrs;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import br.com.ggdio.security.annotations.Secured;
import br.com.ggdio.security.session.Session;
import br.com.ggdio.security.session.SessionManager;
import br.com.ggdio.specs.application.exception.DetailedApplicationException;

@Provider
@Secured(app="nil", action="nil")
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
	
	@Context
    private ResourceInfo resourceInfo;
	
	@Context
	private UriInfo uri;
	
	@Context
	private HttpHeaders headers;

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		SessionManager manager = SessionManager.getSingletonInstance();
		if(manager == null) return;
		
		Session session = manager.get(headers);
		if(session == null) {
			throw new DetailedApplicationException(br.com.ggdio.security.client.Error.INVALID_CREDENTIALS, uri); //401
		}
		
		Secured secured = resourceInfo.getResourceMethod().getAnnotation(Secured.class);
		if(secured != null) {
			String app = secured.app();
			String action = secured.action();
			
			if(app != null && !app.equals("nil") && !session.hasApp(app)) {
				throw new DetailedApplicationException(br.com.ggdio.security.client.Error.ACCESS_FORBIDDEN, uri); //403
			}
			
			if(action != null && !action.equals("nil") && !session.hasPermission(action)) {
				throw new DetailedApplicationException(br.com.ggdio.security.client.Error.ACCESS_FORBIDDEN, uri); //403
			}
		}
	} 

}