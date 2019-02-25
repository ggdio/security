package br.com.ggdio.security.domain.repository;

import br.com.ggdio.security.domain.model.User;
import br.com.ggdio.specs.infrastructure.repository.Transactional;

/**
 * {@link User} Repository.
 * 
 * @see User
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 14 Aug 2018
 * @since 1.0.0-RELEASE
 */
public interface UserRepository extends Transactional {

	public void save(User user);
	
	public User findByKey(String key);
	
}