package br.com.ggdio.security.domain.model;

import java.time.LocalDateTime;

public class User {

	private final Long id;
	private final String key;
	
	private String name;
	private String email;
	
	private LocalDateTime lastLogin;
	private final LocalDateTime createdAt;
	
	public User(String key) {
		this(null, key, LocalDateTime.now());
	}

	public User(Long id, String key, LocalDateTime createdAt) {
		this.id = id;
		this.key = key;
		this.createdAt = createdAt;
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
	
}