package br.com.ggdio.security.domain.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Template {

	private final Long id;
	private final String key;
	
	private String description;
	
	private final Set<Group> groups;
	
	public Template(String key) {
		this(null, key);
	}
	
	public Template(Long id, String key) {
		this(null, key, new HashSet<>());
	}

	public Template(Long id, String key, Set<Group> groups) {
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
	
}