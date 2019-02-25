package br.com.ggdio.security.infrastructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.ggdio.security.domain.model.Session;
import br.com.ggdio.security.domain.repository.SessionRepository;
import br.com.ggdio.security.infrastructure.entity.SessionEntity;
import br.com.ggdio.specs.common.ComponentRegistry;
import br.com.ggdio.specs.infrastructure.repository.JPARepository;

@Repository
public class DefaultSessionRepository extends JPARepository<Session, SessionEntity> implements SessionRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	public DefaultSessionRepository(ComponentRegistry registry) {
		super(registry);
	}
	
	@Override
	@Transactional
	public void save(Session domain) {
		super.save(domain);
	}
	
	@Override
	@Transactional
	public void clearGroups(Session session) {
		SessionEntity entity = newInstance(session);
		entity.getGroups().clear();
		super.save(entity, session);
	}

	@Override
	public Session findActiveByToken(String token) {
		String query = "SELECT e "
					 + "FROM SessionEntity e "
					 + "WHERE e.token = :token "
					 + "AND e.destroyed <> TRUE";

		return super.querySingleResult(query, "token", token);
	}
	
	@Override
	public Session findActiveByRefreshToken(String refreshToken) {
		String query = "SELECT e "
				 + "FROM SessionEntity e "
				 + "WHERE e.refreshToken = :refreshToken "
				 + "AND e.destroyed <> TRUE";

		return super.querySingleResult(query, "refreshToken", refreshToken);
	}
	
	@Override
	public Session findActiveByUser(Long userId) {
		String query = "SELECT e "
					 + "FROM SessionEntity e "
					 + "WHERE e.user.id = :userId "
					 + "AND e.destroyed <> TRUE";

		return super.querySingleResult(query, "userId", userId);
	}
	
	@Override
	public Session findByUser(Long userId) {
		String query = "SELECT e "
					 + "FROM SessionEntity e "
					 + "WHERE e.user.id = :userId";

		return super.querySingleResult(query, "userId", userId);
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

}
