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
import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.security.domain.model.Session;
import br.com.ggdio.specs.application.view.ItemView;

@XmlRootElement(name="scope")
@XmlAccessorType(XmlAccessType.FIELD)
public class SessionScopeView implements ItemView<Object> {

	@XmlElement(name="apps")
	private Set<String> apps;
	
	@XmlElement(name="roles")
	private Set<String> roles;
	
	@XmlElement(name="actions")
	private Map<String, Boolean> actions;
	
	public SessionScopeView() {
		
	}

	public SessionScopeView(Session session) {
		this.apps = new HashSet<>();
		session.getApps().forEach(a -> apps.add(a.getKey()));
		
		this.actions = new HashMap<>();
		this.roles = new HashSet<>(session.getRoles().size());
		for (Role role : session.getRoles()) {
			roles.add(role.getKey());
			
			for(Action action : role.getActions()) {
				actions.put(action.getKey(), Boolean.TRUE);
			}
		}
	}

	public Set<String> getApplications() {
		return new HashSet<>(apps);
	}

	public Set<String> getRoles() {
		return new HashSet<>(roles);
	}
	
	public Map<String, Boolean> getActions() {
		return actions;
	}

	public void setApplications(Set<String> apps) {
		this.apps = new HashSet<>(apps);
	}

	public void setRoles(Set<String> roles) {
		this.roles = new HashSet<>(roles);
	}
	
	public void setActions(Map<String, Boolean> actions) {
		this.actions = actions;
	}
	
}