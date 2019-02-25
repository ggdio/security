package br.com.ggdio.security.infrastructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.ggdio.security.domain.model.User;
import br.com.ggdio.security.domain.repository.UserRepository;
import br.com.ggdio.security.infrastructure.entity.UserEntity;
import br.com.ggdio.specs.infrastructure.repository.JPARepository;

@Repository
public class DefaultUserRepository extends JPARepository<User, UserEntity> implements UserRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public void save(User domain) {
		super.save(domain);
	}
	
	@Override
	public User findByKey(String key) {
		String query = "SELECT e "
					 + "FROM UserEntity e "
					 + "WHERE e.key = :key";
	
		return super.querySingleResult(query, "key", key);
	}
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

}