package br.com.ggdio.security.application.rest.resource;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.ggdio.security.application.rest.view.LoginView;
import br.com.ggdio.security.application.rest.view.SessionView;
import br.com.ggdio.security.application.rest.view.enumeration.Error;
import br.com.ggdio.security.domain.dto.Login;
import br.com.ggdio.security.domain.exception.AuthenticationException;
import br.com.ggdio.security.domain.exception.AuthorizationException;
import br.com.ggdio.security.domain.exception.ExpiredRefreshWindowException;
import br.com.ggdio.security.domain.exception.InvalidTokenException;
import br.com.ggdio.security.domain.service.LoginService;
import br.com.ggdio.specs.application.exception.DetailedApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Authentication Endpoints
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 07 Aug 2018
 * @since 1.0.0-RELEASE
 */
@Path("/auth")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Api(value = "Login Resource")
public class AuthenticationResource {
	
	@Autowired
	private LoginService service;
	
	@POST
	@Path("")
	@ApiOperation(value = "Performs login and generates a session with a valid token. Version 1.", response = SessionView.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Session created or recovered successfully", response = SessionView.class),
        @ApiResponse(code = 400, message = "Invalid Arguments"),
        @ApiResponse(code = 401, message = "Invalid credentials"),
        @ApiResponse(code = 403, message = "Access forbidden for given credentials")
    })
	public Response login(@Context UriInfo uri, @Context HttpHeaders headers, @Valid LoginView data) {
		try {
			if(data == null) throw new AuthenticationException();
			
			Login login = data.unwrap();
			login.setUserAgent(headers.getHeaderString("User-Agent"));
			login.setHost(headers.getHeaderString("Host"));
			
			return Response.ok(new SessionView(service.login(login))).build(); //200
			
		} catch(AuthenticationException e) {
			throw new DetailedApplicationException(Error.INVALID_CREDENTIALS, uri); //401
			
		} catch(AuthorizationException e) {
			throw new DetailedApplicationException(Error.ACCESS_FORBIDDEN, uri); //403
		}
		
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Check current session status. Version 1.", response = SessionView.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Current session is active", response = SessionView.class),
        @ApiResponse(code = 400, message = "Invalid Arguments"),
        @ApiResponse(code = 401, message = "Invalid token"),
        @ApiResponse(code = 403, message = "Invalid token")
    })
	public Response validate(@Context UriInfo uri, @Context HttpHeaders headers) {
		try {
			List<String> authorization = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
			if(authorization == null || authorization.isEmpty()) throw new InvalidTokenException("Authorization Header is EMPTY");
			
			String token = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
			if(token == null || token.isEmpty() || token.split("\\s").length < 2) throw new InvalidTokenException("Invalid Authorization Header Value");
			token = token.split("\\s")[1];
			
			return Response.ok(new SessionView(service.validate(token))).build();
			
		} catch(InvalidTokenException e) {
			throw new DetailedApplicationException(Error.INVALID_TOKEN, uri);
			
		}
		
	}
	
	@PATCH
	@Path("")
	@ApiOperation(value = "Refreshes current session by using the refresh token. Version 1.", response = SessionView.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Session refreshed or recovered successfully", response = SessionView.class),
        @ApiResponse(code = 400, message = "Invalid Arguments"),
        @ApiResponse(code = 401, message = "Invalid refresh token"),
        @ApiResponse(code = 403, message = "Invalid refresh token")
    })
	public Response refresh(@Context UriInfo uri, @Context HttpHeaders headers) {
		try {
			List<String> authorization = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
			if(authorization == null || authorization.isEmpty()) throw new InvalidTokenException("Authorization Header is EMPTY");
			
			String token = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
			if(token == null || token.isEmpty() || token.split("\\s").length < 2) throw new InvalidTokenException("Invalid Authorization Header Value");
			token = token.split("\\s")[1];
			
			return Response.ok(new SessionView(service.refresh(token))).build();
			
		} catch(InvalidTokenException | ExpiredRefreshWindowException e) {
			throw new DetailedApplicationException(Error.INVALID_TOKEN, uri);
			
		}
		
	}
	
	@DELETE
	@Path("")
	@ApiOperation(value = "Performs logout and destroys current session. Version 1.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Session destroyed successfully")
    })
	public Response logout(@Context UriInfo uri, @Context HttpHeaders headers) {
		try {
			List<String> authorization = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
			if(authorization != null && !authorization.isEmpty()) {
				String token = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
				if(token != null && !token.isEmpty() && token.split("\\s").length == 2) {
					token = token.split("\\s")[1];
					service.destroy(token);
				}
			}
			
		} catch(Exception e) {
			// e.ignore();
		}
		
		return Response.ok().build();
	}
	
}