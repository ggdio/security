package br.com.ggdio.security.domain.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Group {

	private final Long id;
	private final String key;
	
	private String description;
	
	private final Set<App> apps;
	private final Set<Role> roles;
	
	public Group(String key) {
		this(null, key);
	}

	public Group(Long id, String key) {
		this(id, key, new HashSet<>(), new HashSet<>());
	}

	public Group(Long id, String key, Set<App> apps, Set<Role> roles) {
		this.id = id;
		this.key = key;
		this.apps = new HashSet<>(apps);
		this.roles = new HashSet<>(roles);
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
	
	public Set<App> getApps() {
		return new HashSet<>(apps);
	}
	
	public boolean containsApp(String app) {
		if(app == null || app.isEmpty()) return false;
		
		for (App instance : getApps()) {
			if(app.equals(instance.getKey())) return true;
		}
		
		return false;
	}
	
	public Set<Role> getRoles() {
		return new HashSet<>(roles);
	}
	
	public boolean containsRole(String role) {
		if(role == null || role.isEmpty()) return false;
		
		for (Role instance : getRoles()) {
			if(role.equals(instance.getKey())) return true;
		}
		
		return false;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addApps(Collection<App> apps) {
		for (App app : apps) {
			addApp(app);
		}
	}
	
	public void addApp(App app) {
		if(app == null) throw new NullPointerException("APP cannot be null");
		
		apps.add(app);
	}
	
	public void removeApp(App app) {
		if(app == null) return;
		
		apps.remove(app);
	}
	
	public void addRoles(Collection<Role> roles) {
		for (Role role : roles) {
			addRole(role);
		}
	}
	
	public void addRole(Role role) {
		if(role == null) throw new NullPointerException("ROLE cannot be null");
		
		roles.add(role);
	}
	
	public void removeRole(Role role) {
		if(role == null) return;
		
		roles.remove(role);
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
		Group other = (Group) obj;
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