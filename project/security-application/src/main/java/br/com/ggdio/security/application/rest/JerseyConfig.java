package br.com.ggdio.security.application.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import br.com.ggdio.security.application.rest.resource.AuthenticationResource;
import br.com.ggdio.specs.jaxrs.CORSFilter;
import br.com.ggdio.specs.jaxrs.ContentTypeFilter;
import br.com.ggdio.specs.jaxrs.DetailedApplicationExceptionMapper;
import br.com.ggdio.specs.jaxrs.DomainExceptionMapper;
import br.com.ggdio.specs.jaxrs.InfrastructureExceptionMapper;
import br.com.ggdio.specs.jaxrs.JSONProvider;
import br.com.ggdio.specs.jaxrs.XMLProvider;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

/**
 * It bootstraps the REST endpoints at application layer
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 30 Jul 2018
 * @since 1.0.0-RELEASE
 */
@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig() throws ClassNotFoundException {
		packages(JerseyConfig.class.getPackage().getName());
		registerResources();
		configureSwagger();
	}

	private void registerResources() {
		// serializer
		register(XMLProvider.class);
		register(JSONProvider.class);
		register(ContentTypeFilter.class);
		register(CORSFilter.class);
		register(DetailedApplicationExceptionMapper.class);
		register(DomainExceptionMapper.class);
		register(InfrastructureExceptionMapper.class);
		
		// resources
		register(AuthenticationResource.class);
	}
	
	private void configureSwagger() {
    	register(ApiListingResource.class);
    	
    	BeanConfig beanConfig = new BeanConfig();
    	beanConfig.setVersion("0.0.1");
    	beanConfig.setSchemes(new String[]{"http", "https"});
    	beanConfig.setHost("127.0.0.1:8080");
    	beanConfig.setBasePath("/api/v1");
    	beanConfig.setDescription("Security Microservice");
    	beanConfig.setResourcePackage("br.com.ggdio.security.application.rest.resource");
    	beanConfig.setPrettyPrint(true);
    	beanConfig.setScan(true);
	}

}