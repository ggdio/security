package br.com.ggdio.specs.jaxrs;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import br.com.ggdio.specs.application.view.ListView;

/**
 * ContentType Filter Provider for JAXRS
 * 
 * @author Guilherme Dio
 *
 */
@Provider
public class ContentTypeFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		List<Object> header = responseContext.getHeaders().get("Content-Type");
		if(header != null && !header.isEmpty()) {
			MediaType contentType = (MediaType) header.get(0);
			if(MediaType.APPLICATION_JSON_TYPE.equals(contentType)) {
				Object entity = responseContext.getEntity();
				if(entity instanceof ListView<?>) {
					ListView<?> list = (ListView<?>) entity;
					responseContext.setEntity(list.getItemList());
				}
			}
		}
	}

}