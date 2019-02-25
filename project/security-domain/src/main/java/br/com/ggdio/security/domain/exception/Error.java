package br.com.ggdio.security.domain.exception;

/**
 * Business error codes
 * 
 * @author Guilherme Dio
 *
 */
public enum Error {

	/**
	 * CODE 000-099
	 * Relates to user and authentication issues
	 */
	LOGIN_NOT_NULL("SEC-000", "Username/Password cannot be null"),
	INVALID_CREDENTIALS("SEC-001", "Invalid Credentials"),
	AUTHENTICATION_PROBLEMS("SEC-002", "Authentication failed due to '%s'"),
	USER_DOESNT_EXISTS("SEC-003", "User '%s' doesn't exists"),
	
	/**
	 * CODE 100-199
	 * Relates to token issues
	 */
	INVALID_TOKEN("SEC-100", "Invalid or expired token. [token=%s]"),
	EXPIRED_REFRESH_WINDOW("SEC-101", "Cannot refresh token due to expired refresh window. User must re-login. [refreshToken=%s]"),
	
	/**
	 * Code 200-299
	 * Relates to permissioning issues
	 */
	USER_LACKS_ROLES("SEC-200", "No ROLES found for user '%s'")
	;
	
	private final String code;
	private final String message;
	
	private Error(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMessage(Object...args) {
		if(args == null || args.length == 0) return message;
		
		return String.format(message, args);
	}
	
}