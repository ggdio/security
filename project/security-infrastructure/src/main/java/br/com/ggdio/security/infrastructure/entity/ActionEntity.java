package br.com.ggdio.security.infrastructure.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import br.com.ggdio.security.domain.model.Action;
import br.com.ggdio.specs.infrastructure.converter.BooleanToCharConverter;
import br.com.ggdio.specs.infrastructure.entity.AbstractEntity;

/**
 * {@link Action} Infrastructure Entity.
 * </br>
 * It defines a data transfer object for infrastructure layer
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 16 Aug 2018
 * @since 1.0.0-RELEASE
 */
@Entity
@Table(name="TB_ACTION", indexes= {@Index(columnList="DS_KEY", unique=true)})
public class ActionEntity extends AbstractEntity<Action> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_ACTION")
	private Long id;
	
	@Column(name="DS_KEY", nullable=false)
	private String key;
	
	@Column(name="DS_ACTION")
	private String description;
	
	@Column(name="DO_INACTIVE")
	@Convert(converter=BooleanToCharConverter.class)
	private boolean inactive;
	
	public ActionEntity() {
		
	}
	
	public ActionEntity(Action domain) {
		this.id = domain.getId();
		this.key = domain.getKey();
		this.description = domain.getDescription();
		this.inactive = false;
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

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	@Override
	public Action unwrap() {
		Action action = new Action(getId(), getKey());
		action.setDescription(getDescription());
		return action;
	}

}
