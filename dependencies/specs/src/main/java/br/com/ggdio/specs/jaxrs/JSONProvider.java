package br.com.ggdio.specs.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * JSON Provider for JAXRS
 * 
 * @author Guilherme Dio
 *
 */
@Provider
@Consumes(MediaType.WILDCARD)
@Produces(MediaType.WILDCARD)
public class JSONProvider extends JacksonJaxbJsonProvider {

	@Override
	protected ObjectMapper _locateMapperViaProvider(Class<?> type, MediaType mediaType) {
		ObjectMapper mapper = super._locateMapperViaProvider(type, mediaType);
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		return mapper;
	}
	
}