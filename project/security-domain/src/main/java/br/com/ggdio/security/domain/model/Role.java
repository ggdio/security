package br.com.ggdio.security.domain.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Role Domain Model.
 * <p>
 * A role defines an acess profile with a set of actions({@link Action})
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 09 Aug 2018
 * @since 1.0.0-RELEASE
 */
public class Role {

	private final Long id;
	private final String key;
	
	private String description;
	
	private final Set<Action> actions;
	
	public Role(String key) {
		this(null, key);
	}

	public Role(Long id, String key) {
		this(id, key, new HashSet<>());
	}

	public Role(Long id, String key, Set<Action> actions) {
		this.id = id;
		this.key = key;
		this.actions = new HashSet<>(actions);
	}

	public Long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}
	
	public Set<Action> getActions() {
		return new HashSet<>(actions);
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addAction(Collection<Action> actions) {
		for (Action action : actions) {
			addAction(action);
		}
	}
	
	public void addAction(Action action) {
		if(action == null) throw new NullPointerException("ACTION cannot be null");
		
		actions.add(action);
	}
	
	public void removeAction(Action action) {
		if(action == null) return;
		
		actions.remove(action);
	}

}