package br.com.ggdio.security.domain.model;

/**
 * Destroy Reasons for {@link Session}
 * 
 * @author Guilherme Dio
 *
 */
public enum DestroyReason {

	EXPIRED("Expired Session"),
	
	LOGOUT("User Logged Out"),
	
	FORCED("Forced by Sistem")
	
	;
	
	private final String description;
	
	private DestroyReason(final String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static DestroyReason fromString(String value) {
		return valueOf(value.toUpperCase());
	}
	
}
