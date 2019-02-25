package br.com.ggdio.specs.infrastructure.repository;

import javax.persistence.EntityManager;

/**
 * JPA Persistence Layer
 * 
 * @author Guilherme Dio
 *
 */
public interface Persistence {

	public EntityManager getEntityManager();
	
}