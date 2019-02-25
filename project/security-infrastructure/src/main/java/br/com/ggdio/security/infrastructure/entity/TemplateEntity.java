package br.com.ggdio.security.infrastructure.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Template;
import br.com.ggdio.specs.infrastructure.converter.BooleanToCharConverter;
import br.com.ggdio.specs.infrastructure.entity.AbstractEntity;

@Entity
@Table(name="TB_TEMPLATE", indexes= {@Index(columnList="DS_KEY", unique=true)})
public class TemplateEntity extends AbstractEntity<Template> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_TEMPLATE")
	private Long id;
	
	@Column(name="DS_KEY", nullable=false)
	private String key;
	
	@Column(name="DS_TEMPLATE")
	private String description;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "TB_TEMPLATE_GROUP",
	    joinColumns = @JoinColumn(name = "ID_TEMPLATE"),
	    inverseJoinColumns = @JoinColumn(name = "ID_GROUP")
	)
	private Set<GroupEntity> groups;
	
	@Column(name="DO_INACTIVE")
	@Convert(converter=BooleanToCharConverter.class)
	private boolean inactive;
	
	public TemplateEntity() {
		
	}
	
	public TemplateEntity(Template domain) {
		this.id = domain.getId();
		this.key = domain.getKey();
		this.description = domain.getDescription();
		this.groups = new HashSet<>();
		domain.getGroups().forEach(g -> groups.add(new GroupEntity(g)));
		this.inactive = false;
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

	public Set<GroupEntity> getGroups() {
		return new HashSet<>(groups);
	}

	public boolean isInactive() {
		return inactive;
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

	public void setGroups(Set<GroupEntity> groups) {
		this.groups = new HashSet<>(groups);
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	@Override
	public Template unwrap() {
		Set<Group> groups = new HashSet<>();
		this.groups.forEach(g -> groups.add(g.unwrap()));
		Template template = new Template(getId(), getKey(), groups);
		template.setDescription(getDescription());
		return template;
	}
	
}