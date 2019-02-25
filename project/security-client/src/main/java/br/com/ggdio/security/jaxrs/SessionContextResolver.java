package br.com.ggdio.security.jaxrs;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import br.com.ggdio.security.session.Session;
import br.com.ggdio.security.session.SessionManager;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SessionContextResolver implements ContextResolver<Session> {
	
	@Context 
	private HttpHeaders headers;

	@Override
	public Session getContext(Class<?> type) {
		return SessionManager.getSingletonInstance().get(headers);
	}

}