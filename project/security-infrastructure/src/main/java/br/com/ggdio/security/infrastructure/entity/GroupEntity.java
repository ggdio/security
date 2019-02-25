package br.com.ggdio.security.infrastructure.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.com.ggdio.security.domain.model.App;
import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.specs.infrastructure.entity.AbstractEntity;

@Entity
@Table(name="TB_GROUP", indexes= {@Index(columnList="DS_KEY", unique=true)})
public class GroupEntity extends AbstractEntity<Group> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_GROUP")
	private Long id;
	
	@Column(name="DS_KEY", nullable=false)
	private String key;
	
	@Column(name="DS_GROUP")
	private String description;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "TB_GROUP_ROLE",
	    joinColumns = @JoinColumn(name = "ID_GROUP"),
	    inverseJoinColumns = @JoinColumn(name = "ID_ROLE")
	)
	private Set<RoleEntity> roles;
	
	@ManyToMany(mappedBy="groups", fetch=FetchType.EAGER)
	private Set<AppEntity> apps;
	
	public GroupEntity() {
		
	}
	
	public GroupEntity(Group domain) {
		this.id = domain.getId();
		this.key = domain.getKey();
		this.description = domain.getDescription();
		
		this.roles = new HashSet<>();
		domain.getRoles().forEach(r -> roles.add(new RoleEntity(r)));
		
		this.apps = new HashSet<>();
		domain.getApps().forEach(a -> apps.add(new AppEntity(a)));
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

	public Set<RoleEntity> getRoles() {
		return new HashSet<>(roles);
	}
	
	public Set<AppEntity> getApps() {
		return new HashSet<>(apps);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = new HashSet<>(roles);
	}
	
	public void setApps(Set<AppEntity> apps) {
		this.apps = new HashSet<>(apps);
	}

	@Override
	public Group unwrap() {
		Set<Role> roles = new HashSet<>();
		getRoles().forEach(r -> roles.add(r.unwrap()));
		
		Set<App> apps = new HashSet<>();
		getApps().forEach(a -> apps.add(a.unwrap()));
		
		Group group = new Group(getId(), getKey(), apps, roles);
		group.setDescription(getDescription());
		
		return group;
	}

}