package br.com.ggdio.security.infrastructure.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import br.com.ggdio.security.domain.model.User;
import br.com.ggdio.specs.infrastructure.converter.LocalDateTimeConverter;
import br.com.ggdio.specs.infrastructure.entity.AbstractEntity;

@Entity
@Table(name="TB_USER", indexes= {@Index(columnList="DS_KEY", unique=true)})
public class UserEntity extends AbstractEntity<User> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_USER")
	private Long id;
	
	@Column(name="DS_KEY", nullable=false)
	private String key;
	
	@Column(name="DS_USER")
	private String name;
	
	@Column(name="DS_EMAIL")
	private String email;
	
	@Column(name="DT_LASTLOGIN")
    @Convert(converter=LocalDateTimeConverter.class)
    private LocalDateTime lastLogin;
	
	@Column(name="DT_CREATED", updatable=false)
    @Convert(converter=LocalDateTimeConverter.class)
    private LocalDateTime createdAt;
	
	public UserEntity() {
		
	}

	public UserEntity(User domain) {
		this.id = domain.getId();
		this.key = domain.getKey();
		this.name = domain.getName();
		this.email = domain.getEmail();
		this.lastLogin = domain.getLastLogin();
		this.createdAt = domain.getCreatedAt();
	}

	public Long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public User unwrap() {
		User user = new User(getId(), getKey(), getCreatedAt());
		user.setEmail(getEmail());
		user.setLastLogin(getLastLogin());
		user.setName(getName());
		
		return user;
	}
	
}