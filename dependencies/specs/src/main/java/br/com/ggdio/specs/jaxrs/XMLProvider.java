package br.com.ggdio.specs.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider;

/**
 * XML Provider for JAXRS
 * 
 * @author Guilherme Dio
 *
 */
@Provider
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class XMLProvider extends JacksonJaxbXMLProvider {

}