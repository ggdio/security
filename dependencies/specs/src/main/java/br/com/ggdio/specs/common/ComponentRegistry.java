package br.com.ggdio.specs.common;

/**
 * Components registry.
 * 
 * It provides an access interface for components registering and lookup.
 * 
 * @author Guilherme Dio
 * @version 1.0.3-RELEASE, 14 Aug 2018
 * @since 1.0.3-RELEASE
 *
 */
public interface ComponentRegistry {

	public <T> T register(T component);
	public <T> T lookup(Class<T> clazz);
	
}