package br.com.ggdio.security.infrastructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.ggdio.security.domain.model.Template;
import br.com.ggdio.security.domain.repository.TemplateRepository;
import br.com.ggdio.security.infrastructure.entity.TemplateEntity;
import br.com.ggdio.specs.infrastructure.repository.JPARepository;

@Repository
public class DefaultTemplateRepository extends JPARepository<Template, TemplateEntity> implements TemplateRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public void save(Template domain) {
		super.save(domain);
	}

	@Override
	public Template findByKey(String key) {
		String query = "SELECT e "
					 + "FROM TemplateEntity e "
					 + "WHERE e.key = :key";
		
		return super.querySingleResult(query, "key", key);
	}
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

}
