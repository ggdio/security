package br.com.ggdio.security.application.rest.resource;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.ggdio.security.application.rest.view.RoleView;
import br.com.ggdio.security.application.rest.view.enumeration.Error;
import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.security.domain.repository.GroupRepository;
import br.com.ggdio.security.domain.repository.RoleRepository;
import br.com.ggdio.specs.application.exception.DetailedApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * API for Roles
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 27 Sep 2018
 * @since 1.0.0-RELEASE
 */
@Path("/roles")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Api(value = "Roles Resource")
public class RoleResource {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@GET
	@Path("/{key}")
	@ApiOperation(value = "Find a Role by its Key. Version 1.", response = RoleView.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Role resource found"),
        @ApiResponse(code = 404, message = "Role resource not found")
    })
	public Response findByKey(@PathParam("key") String key, @Context UriInfo uri) {
		Role role = roleRepository.findByKey(key);
		if(role == null) {
			throw new DetailedApplicationException(Error.ROLE_NOT_FOUND, uri, key);
		}
		
		Set<Group> groups = groupRepository.findByRole(role.getId());
		
		return Response.ok(new RoleView(role, groups)).build();
	}
	
}