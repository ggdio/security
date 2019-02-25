package br.com.ggdio.specs.template;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import br.com.ggdio.specs.template.exception.RendererException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Freemarker renderer
 * 
 * @author Marcus Zanini
 *
 */
public class FreemakerRenderer implements Renderer {
	
	public FreemakerRenderer(Map<String, String> args) {
		
	}
	
	@Override
	public String render(String template, Map<String, Object> args) {
		String resultRender = null;
		try {
			Template freeMarkerTemplate = new Template("name", new StringReader(template), new Configuration(Configuration.VERSION_2_3_25));

			Writer out = new StringWriter();
			freeMarkerTemplate.process(args, out);
			resultRender = ((StringWriter) out).getBuffer().toString();
			
		} catch(IOException ex) {
			throw new RendererException("Failed to load Freemarker template due to " + ex.getMessage(), ex);
		} catch(TemplateException te) {
			throw new RendererException("Failed to process Freemarker template due to " + te.getMessage(), te);
		}
		
		return resultRender;
	}

}
