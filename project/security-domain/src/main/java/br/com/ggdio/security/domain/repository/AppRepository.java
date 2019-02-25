package br.com.ggdio.security.domain.repository;

import br.com.ggdio.security.domain.model.App;
import br.com.ggdio.security.domain.model.Group;

/**
 * {@link App} Repository.
 * 
 * @see Group
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 14 Aug 2018
 * @since 1.0.0-RELEASE
 */
public interface AppRepository {

	public App findByKey(String key);
	
	public void save(App app);
	
}
