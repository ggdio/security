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

import br.com.ggdio.security.domain.model.Action;
import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.specs.infrastructure.entity.AbstractEntity;

@Entity
@Table(name="TB_ROLE", indexes= {@Index(columnList="DS_KEY", unique=true)})
public class RoleEntity extends AbstractEntity<Role> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_ROLE")
	private Long id;
	
	@Column(name="DS_KEY", nullable=false)
	private String key;
	
	@Column(name="DS_ROLE")
	private String description;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "TB_ROLE_ACTION",
	    joinColumns = @JoinColumn(name = "ID_ROLE"),
	    inverseJoinColumns = @JoinColumn(name = "ID_ACTION")
	)
	private Set<ActionEntity> actions;
	
	public RoleEntity() {
		
	}
	
	public RoleEntity(Role domain) {
		this.id = domain.getId();
		this.key = domain.getKey();
		this.description = domain.getDescription();
		
		this.actions = new HashSet<>();
		domain.getActions().forEach(a -> actions.add(new ActionEntity(a)));
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
	
	public Set<ActionEntity> getActions() {
		return new HashSet<>(actions);
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
	
	public void setActions(Set<ActionEntity> actions) {
		this.actions = new HashSet<>(actions);
	}

	@Override
	public Role unwrap() {
		Set<Action> actions = new HashSet<>();
		getActions().forEach(a -> actions.add(a.unwrap()));
		
		Role role = new Role(getId(), getKey(), actions);
		role.setDescription(getDescription());
		
		return role;
	}

}