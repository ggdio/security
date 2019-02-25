package br.com.ggdio.security.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.ggdio.specs.common.ComponentRegistry;

@Component
public class BeanComponentRegistry implements ComponentRegistry {
	
	@Autowired
	private ApplicationContext context;

	@Override
	public <T> T register(T component) {
		return null; // spring already did it
	}

	@Override
	public <T> T lookup(Class<T> clazz) {
		return context.getBean(clazz);
	}

}