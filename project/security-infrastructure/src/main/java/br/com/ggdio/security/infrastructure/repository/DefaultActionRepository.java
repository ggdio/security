package br.com.ggdio.security.infrastructure.repository;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.ggdio.security.domain.model.Action;
import br.com.ggdio.security.domain.repository.ActionRepository;
import br.com.ggdio.security.infrastructure.entity.ActionEntity;
import br.com.ggdio.specs.infrastructure.repository.JPARepository;

@Repository
public class DefaultActionRepository extends JPARepository<Action, ActionEntity> implements ActionRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public void save(Action domain) {
		super.save(domain);
	}
	
	@Override
	public Set<Action> findAll() {
		String query = "SELECT e "
				+ "FROM ActionEntity e "
				+ "ORDER BY e.key";
		
		return super.query(query);
	}
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

}
