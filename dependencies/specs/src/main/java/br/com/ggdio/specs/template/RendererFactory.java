package br.com.ggdio.specs.template;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for template renderers
 * 
 * @author Guilherme Dio
 *
 */
public class RendererFactory {
	
	public static Renderer getDefault() {
		return getDefault(new HashMap<>());
	}
	
	public static Renderer getDefault(Map<String, String> args) {
		return get(RendererType.FREEMARKER, args);
	}
	
	public static Renderer get(RendererType type) {
		return get(type, new HashMap<>());
	}
	
	public static Renderer get(RendererType type, Map<String, String> args) {
		switch(type) {
			case FREEMARKER: return new FreemakerRenderer(args);
			
			default: return null;
		}
	}
	
}