package br.com.ggdio.specs.template;

import java.util.Map;

/**
 * Template renderer interface
 * 
 * @author Guilherme Dio
 *
 */
public interface Renderer {

	public String render(String template, Map<String, Object> args);
	
}