package br.com.ggdio.security.domain.repository;

import br.com.ggdio.security.domain.model.Role;

public interface RoleRepository {

	public void save(Role role);
	
	public Role findByKey(String key);
	
}
