package br.com.ggdio.security.infrastructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.ggdio.security.domain.model.App;
import br.com.ggdio.security.domain.repository.AppRepository;
import br.com.ggdio.security.infrastructure.entity.AppEntity;
import br.com.ggdio.specs.infrastructure.repository.JPARepository;

@Repository
public class DefaultAppRepository extends JPARepository<App, AppEntity> implements AppRepository {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public App findByKey(String key) {
		final String query = "SELECT e "
						   + "FROM AppEntity e "
						   + "WHERE e.inactive <> TRUE "
						   + "AND e.key = :key";
		
		return super.querySingleResult(query, "key", key);
	}
	
	@Override
	@Transactional
	public void save(App app) {
		super.save(app);
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

}
