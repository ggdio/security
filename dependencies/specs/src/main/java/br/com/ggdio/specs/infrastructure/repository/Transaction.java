package br.com.ggdio.specs.infrastructure.repository;

import javax.persistence.EntityManager;

/**
 * Abstraction for JPA EntityManager transaction
 * 
 * @author Guilherme Dio
 *
 */
public class Transaction {

	private final EntityManager em;

	public Transaction(EntityManager em) {
		this.em = em;
	}
	
	public EntityManager getEntityManager() {
		return em;
	}
	
	/**
	 * Begins a new transaction if none active
	 */
	public void begin() {
		if(!isActive()) {
			getEntityManager().getTransaction().begin();
		}
	}
	
	/**
	 * Commits the current transaction
	 */
	public void commit() {
		if (isActive()) {
			getEntityManager().getTransaction().commit();
			getEntityManager().clear();
		}
	}
	
	/**
	 * Rollbacks the current transaction
	 */
	protected void rollback() {
		if (isActive()) {
			getEntityManager().getTransaction().rollback();
			getEntityManager().clear();
		}
	}
	
	/**
	 * Checks if transaction is already opened
	 * @return true/false
	 */
	public boolean isActive() {
		return getEntityManager() != null && getEntityManager().getTransaction() != null && getEntityManager().getTransaction().isActive();
	}
	
}