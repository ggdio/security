package br.com.ggdio.security.application.rest.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.ggdio.security.domain.model.Action;
import br.com.ggdio.security.domain.model.App;
import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.specs.application.view.ItemView;

/**
 * View for Role
 * 
 * @author Guilherme Dio
 *
 */
@XmlRootElement(name="role")
@XmlAccessorType(XmlAccessType.FIELD)
public class RoleView implements ItemView<Role> {
	
	@XmlElement(name="key")
	private String key;
	
	@XmlElement(name="description")
	private String description;
	
	@XmlElement(name="apps")
	private Set<String> apps;
	
	@XmlElement(name="groups")
	private Set<String> groups;
	
	@XmlElement(name="actions")
	private Map<String, Boolean> actions;
	
	public RoleView(Role role, Set<Group> groups) {
		this.key = role.getKey();
		this.description = role.getDescription();
		
		this.apps = new HashSet<>();
		this.groups = new HashSet<>();
		if(groups != null && !groups.isEmpty()) {
			for (Group group : groups) {
				Set<App> apps = group.getApps(); 
				if(apps != null) {
					for (App app : apps) {
						this.apps.add(app.getKey());
					}
				}
				
				this.groups.add(group.getKey());
			}
		}
		
		this.actions = new HashMap<>();
		Set<Action> actions = role.getActions();
		if(actions != null) {
			for(Action action : actions) {
				this.actions.put(action.getKey(), Boolean.TRUE);
			}
		}
	}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

	public Set<String> getApps() {
		return new HashSet<>(apps);
	}

	public Set<String> getGroups() {
		return new HashSet<>(groups);
	}

	public Map<String, Boolean> getActions() {
		return actions;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setApps(Set<String> apps) {
		this.apps = new HashSet<>(apps);
	}

	public void setGroups(Set<String> groups) {
		this.groups = new HashSet<>(groups);
	}

	public void setActions(Map<String, Boolean> actions) {
		this.actions = actions;
	}
	
}