package br.com.ggdio.specs.infrastructure.repository;

/**
 * Interface for transactional persistence layer
 * 
 * @author Guilherme Dio
 *
 */
public interface Transactional extends Persistence {

	public default Transaction getTransaction() {
		return new Transaction(getEntityManager());
	}
	
}