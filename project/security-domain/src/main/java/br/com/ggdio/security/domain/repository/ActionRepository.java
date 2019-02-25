package br.com.ggdio.security.domain.repository;

import java.util.Set;

import br.com.ggdio.security.domain.model.Action;

public interface ActionRepository {

	public void save(Action action);
	
	public Set<Action> findAll();
	
}
