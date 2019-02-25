package br.com.ggdio.security.session;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="role")
@XmlAccessorType(XmlAccessType.FIELD)
public class Role {
	
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
	
	public Role() {
		// TODO Auto-generated constructor stub
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

	@Override
	public String toString() {
		return "Role [key=" + key + ", description=" + description + ", apps=" + apps + ", groups=" + groups
				+ ", actions=" + actions + "]";
	}
	
}