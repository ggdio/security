package br.com.ggdio.security.infrastructure.entity;

import br.com.ggdio.security.domain.model.AuthorizationType;

public enum AuthorizationTypeEntity {

	OAUTH("bearer"),
	
	BASIC("basic"),
	
	DIGEST("digest")
	
	;
	
	private final String description;
	
	private AuthorizationTypeEntity(String description) {
		this.description = description;
	}
	
	public String getKey() {
		return this.toString();
	}
	
	public String getDescription() {
		return description;
	}
	
	public static AuthorizationTypeEntity wrap(AuthorizationType technology) {
		return AuthorizationTypeEntity.valueOf(technology.name());
	}
	
	public AuthorizationType unwrap() {
		return AuthorizationType.valueOf(this.name());
	}
	
	public static AuthorizationTypeEntity fromString(String value) {
		return valueOf(AuthorizationTypeEntity.class, value.toUpperCase());
	}
	
}
