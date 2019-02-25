package br.com.ggdio.security.infrastructure.entity;

import br.com.ggdio.security.domain.model.DestroyReason;

public enum DestroyReasonEntity {

	EXPIRED("Expired Session"),
	
	LOGOUT("User Logged Out"),
	
	FORCED("Forced by Sistem")
	
	;
	
	private final String description;
	
	private DestroyReasonEntity(String description) {
		this.description = description;
	}
	
	public String getKey() {
		return this.toString();
	}
	
	public String getDescription() {
		return description;
	}
	
	public static DestroyReasonEntity wrap(DestroyReason reason) {
		return DestroyReasonEntity.valueOf(reason.name());
	}
	
	public DestroyReason unwrap() {
		return DestroyReason.valueOf(this.name());
	}
	
	public static DestroyReasonEntity fromString(String value) {
		return valueOf(DestroyReasonEntity.class, value.toUpperCase());
	}
	
}
