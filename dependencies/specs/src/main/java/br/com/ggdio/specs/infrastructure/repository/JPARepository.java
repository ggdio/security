package br.com.ggdio.specs.infrastructure.repository;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ggdio.reflection.ReflectionUtils;
import br.com.ggdio.specs.common.ComponentRegistry;
import br.com.ggdio.specs.infrastructure.entity.AbstractEntity;
import br.com.ggdio.specs.infrastructure.exception.InfrastructureException;

/**
 * Repository for Persistence Layer
 * 
 * @author Guilherme Dio
 * @version 1.2.0-RELEASE, 15 Aug 2018
 * @since 1.0.0-RELEASE
 *
 * @param <D> - The Domain Model
 * @param <E> - The Entity Model
 */
public abstract class JPARepository<D, E extends AbstractEntity<D>> implements Transactional {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final Class<E> entityClazz;
	
	private final ComponentRegistry registry;
	
	public JPARepository() {
		this(null);
	}
	
	public JPARepository(ComponentRegistry registry) {
		this.entityClazz = getEntityClazz();
		
		this.registry = registry;
	}

	/**
	 * Save domain data
	 * 
	 * @param domain - Domain Object
	 */
	@javax.transaction.Transactional
	protected void save(D domain) {
		E entity = newInstance(domain);
		
		this.save(entity, domain);
	}
	
	/**
	 * Save domain data w/ transaction
	 * 
	 * @param transaction - Transaction reference
	 * @param domain - Domain Object
	 */
	@javax.transaction.Transactional
	protected void save(Transaction transaction, D domain) {
		E entity = newInstance(domain);
		
		this.save(transaction, entity, domain);
	}
	
	/**
	 * Save entity data
	 * @param entity - Entity Object
	 * @param domainRef - Domain Reference
	 */
	@javax.transaction.Transactional
	protected void save(E entity, D domainRef) {
		this.save(null, entity, domainRef);
	}
	
	/**
	 * Save entity data with a possible transactional flow
	 * @param transaction - Transaction reference
	 * @param entity - Entity Object
	 * @param domainRef - Domain Reference
	 */
	@javax.transaction.Transactional
	protected void save(Transaction transaction, E entity, D domainRef) {
		EntityManager em = getEntityManager(transaction);
		if(entity.getId() == null) {
			em.persist(entity);
			ReflectionUtils.forceSetField(domainRef, getIdFieldName(), entity.getId());
			
		} else {
			em.merge(entity);
			
		}
		
		em.flush();
	}
	
	/**
	 * Find a domain object by its ID
	 * @param id - Entity Key
	 * @return Domain Object
	 */
	protected D findById(Long id) {
		Class<E> clazz = getEntityClazz();
		String entityName = clazz.getSimpleName();
		String idFieldName = getIdFieldName();
		TypedQuery<E> query = getEntityManager().createQuery(
				"SELECT E "
			  + "FROM " + entityName + " E "
			  + "WHERE E."+idFieldName+" = :ID", clazz
			);
		
		query.setParameter("ID", id);
		
		try {
			return unwrap(query.getSingleResult());
			
		} catch(NoResultException e) {
			return null;
			
		}
	
	}
	
	/**
	 * Find all persistent objects for current domain
	 * @return {@link Set} of Domain Object
	 */
	private List<E> _findAll() {
		Class<E> clazz = getEntityClazz();
		String entityName = clazz.getSimpleName();
		TypedQuery<E> query = getEntityManager().createQuery(
				"SELECT E "
			  + "FROM " + entityName + " E ", clazz
			);
		
		
		try {
			return query.getResultList();
			
		} catch(NoResultException e) {
			return null;
			
		}
	}
	
	/**
	 * Find all persistent objects for current domain
	 * @return {@link Set} of Domain Object
	 */
	protected List<D> findAllOrdered() {
		return parseList(_findAll());
	}
	
	/**
	 * Find all persistent objects for current domain
	 * @return {@link Set} of Domain Object
	 */
	protected Set<D> findAll() {
		return parseSet(_findAll());
	}
	
	/**
	 * Executes a JPQL query
	 * 
	 * @param jpql - String w/ the query
	 * @param firstResult - The initial line
	 * @param maxResults - Max number of lines
	 * 
	 * @return {@link Set} of domains
	 * 
	 * @throws InfrastructureException - If anything wrong occurs
	 */
	private List<E> _query(String jpql, Map<String, Object> params, Integer firstResult, Integer maxResults) throws InfrastructureException {
		List<E> entities = null;
		try {
			TypedQuery<E> query = getEntityManager().createQuery(jpql, entityClazz);
			
			if (firstResult > 0) query.setFirstResult(firstResult);
			if (maxResults > 0) query.setMaxResults(maxResults);
			
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
			
			entities = query.getResultList();
			
		} catch(NoResultException | EntityNotFoundException e) {
			return null;
			
		} catch (Exception e) {
			String msg = "An unexpected error occured while executing the HQL: '" + jpql + "' with an row limit of '" + maxResults + "' and first result '" + firstResult + "': "+e.getMessage();
			log.error(msg, e);
			throw new InfrastructureException(msg, e);
			
		}
		
		return entities;
	}
	
	protected Set<D> query(String jpql, Map<String, Object> params, Integer firstResult, Integer maxResults) throws InfrastructureException {
		return parseSet(_query(jpql, params, firstResult, maxResults));
	}
	
	/**
	 * 
	 * @param jpql - String w/ the query
	 * @param maxResults - Max number of lines
	 * 
	 * @return {@link Set} of domains
	 * 
	 * @throws InfrastructureException - If anything wrong occurs
	 */
	protected Set<D> query(String jpql, Map<String, Object> params, Integer maxResults) throws InfrastructureException {
		return query(jpql, params, 0, maxResults);
	}

	/**
	 * 
	 * @param jpql - String w/ the query
	 * 
	 * @return {@link Set} of domains
	 * 
	 * @throws InfrastructureException - If anything wrong occurs
	 */
	protected Set<D> query(String jpql, Map<String, Object> params) throws InfrastructureException {
		return query(jpql, params, 0, 0);
	}
	
	/**
	 * 
	 * @param jpql - String w/ the query
	 * 
	 * @return {@link Set} of domains
	 * 
	 * @throws InfrastructureException - If anything wrong occurs
	 */
	protected List<D> queryOrdered(String jpql, Map<String, Object> params) throws InfrastructureException {
		return parseList(_query(jpql, params, 0, 0));
	}
	
	/**
	 * 
	 * @param jpql - String w/ the query
	 * 
	 * @return {@link Set} of domains
	 * 
	 * @throws InfrastructureException - If anything wrong occurs
	 */
	private List<E> _query(String jpql, Object...args) throws InfrastructureException {
		Map<String, Object> params = new HashMap<>();
		if(args != null && args.length > 0) {
			if(args.length % 2 != 0) throw new IllegalArgumentException("Args must be even.");
			
			byte count = 0;
			do {
				params.put((String) args[count], args[++count]);
				
			} while(count < (args.length - 1));
		}
		return _query(jpql, params, 0, 0);
	}
	
	/**
	 * 
	 * @param jpql - String w/ the query
	 * 
	 * @return {@link Set} of domains
	 * 
	 * @throws InfrastructureException - If anything wrong occurs
	 */
	protected Set<D> query(String jpql, Object...args) throws InfrastructureException {
		return parseSet(_query(jpql, args));
	}
	
	/**
	 * 
	 * @param jpql - String w/ the query
	 * 
	 * @return {@link Set} of domains
	 * 
	 * @throws InfrastructureException - If anything wrong occurs
	 */
	protected List<D> queryOrdered(String jpql, Object...args) throws InfrastructureException {
		return parseList(_query(jpql, args));
	}
	
	/**
	 * 
	 * @param jpql - String w/ the query
	 * 
	 * @return Domain Object
	 * 
	 * @throws InfrastructureException - If anything wrong occurs
	 */
	protected D querySingleResult(String jpql, Object...args) throws InfrastructureException {
		Set<D> result = query(jpql, args);
		if(result == null || result.isEmpty()) 
			return null;
		
		return result.iterator().next();
	}
	
	/**
	 * 
	 * @param jpql - String w/ the query
	 * 
	 * @return Domain Object
	 * 
	 * @throws InfrastructureException - If anything wrong occurs
	 */
	protected D querySingleResult(String jpql, Map<String, Object> params) throws InfrastructureException {
		Set<D> result = query(jpql, params);
		if(result == null || result.isEmpty()) 
			return null;
		
		return result.iterator().next();
	}
	
	protected E newInstance(D domain) {
		try {
			return entityClazz.getConstructor(domain.getClass()).newInstance(domain);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

	@SuppressWarnings("unchecked")
    private Class<E> getEntityClazz() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        String clazz = pt.getActualTypeArguments()[1].toString().split("\\s")[1];
        
        try {
        	return (Class<E>) Class.forName(clazz);
        } catch(Exception e) {
        	throw new RuntimeException(e);
        }
    }
	
	protected Set<D> parseSet(List<E> resultList) {
		if(resultList == null) return null;
		
		Set<D> result = new HashSet<>();
		resultList.forEach(i -> {
			result.add(unwrap(i));
		});
		return result;
	}
	
	protected List<D> parseList(List<E> resultList) {
		if(resultList == null) return null;
		
		List<D> result = new ArrayList<>();
		resultList.forEach(i -> {
			result.add(unwrap(i));
		});
		return result;
	}
	
	protected D unwrap(E entity) {
		if(registry != null) {
			return entity.unwrap(registry);
			
		} else {
			return entity.unwrap();
			
		}
	}
	
	protected String getIdFieldName() {
		return "id";
	}
	
	protected EntityManager getEntityManager(Transaction transaction) {
		if(transaction != null && transaction.isActive()) {
			return transaction.getEntityManager();
		}
		
		return getEntityManager();
	}
	
}