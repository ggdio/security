package br.com.ggdio.security.domain.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class App {

	private final Long id;
	private final String key;
	
	private String description;
	
	private final Set<Group> groups;
	
	public App(String key) {
		this(null, key);
	}
	
	public App(Long id, String key) {
		this(id, key, new HashSet<>());
	}

	public App(Long id, String key, Set<Group> groups) {
		this.id = id;
		this.key = key;
		this.groups = new HashSet<>(groups);
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
	
	public Set<Group> getGroups() {
		return new HashSet<>(groups);
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addGroups(Collection<Group> groups) {
		for (Group group : groups) {
			addGroup(group);
		}
	}
	
	public void addGroup(Group group) {
		if(group == null) throw new NullPointerException("GROUP cannot be null");
		
		groups.add(group);
	}
	
	public void removeGroup(Group group) {
		if(group == null) return;
		
		groups.remove(group);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		App other = (App) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
}