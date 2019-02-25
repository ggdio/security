package br.com.ggdio.security.application.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Health Check Endpoints
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 07 Aug 2018
 * @since 1.0.0-RELEASE
 */
@Path("/status")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class HealthCheckResource {
	
	@GET
	@Path("")
	public Response isAlive(@Context UriInfo uri, @Context HttpHeaders headers) {
		return Response.ok().build();
	}
	
}