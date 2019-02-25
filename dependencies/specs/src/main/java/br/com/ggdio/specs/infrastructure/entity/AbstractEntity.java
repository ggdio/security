package br.com.ggdio.specs.infrastructure.entity;

import br.com.ggdio.specs.common.ComponentRegistry;

/**
 * Entity for Persistence Layer
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 30 Jul 2018
 * @since 1.0.0-RELEASE
 *
 * @param <D> - Domain Model Type
 */
public abstract class AbstractEntity<D> {
	
	public AbstractEntity() {
		
	}
	
	public AbstractEntity(D domain) {
		
	}

	public abstract Object getId();
	
	public abstract D unwrap();
	
	public D unwrap(ComponentRegistry registry) {
		return unwrap();
	}
	
}
