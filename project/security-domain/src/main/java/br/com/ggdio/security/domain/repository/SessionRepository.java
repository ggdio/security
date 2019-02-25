package br.com.ggdio.security.domain.repository;

import br.com.ggdio.security.domain.model.Session;
import br.com.ggdio.specs.infrastructure.repository.Transactional;

/**
 * {@link Session} Repository.
 * 
 * @see Session
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 14 Aug 2018
 * @since 1.0.0-RELEASE
 */
public interface SessionRepository extends Transactional {

	public void save(Session session);
	
	public void clearGroups(Session session);
	
	public Session findActiveByToken(String token);
	
	public Session findActiveByRefreshToken(String refreshToken);
	
	public Session findActiveByUser(Long userId);
	
	public Session findByUser(Long userId);
	
}