package br.com.ggdio.security.infrastructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.security.domain.repository.RoleRepository;
import br.com.ggdio.security.infrastructure.entity.RoleEntity;
import br.com.ggdio.specs.infrastructure.repository.JPARepository;

@Repository
public class DefaultRoleRepository extends JPARepository<Role, RoleEntity> implements RoleRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public void save(Role domain) {
		super.save(domain);
	}
	
	@Override
	public Role findByKey(String key) {
		String query = "SELECT e "
					 + "FROM RoleEntity e "
					 + "WHERE e.key = :key";
	
		return super.querySingleResult(query, "key", key);
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

}
