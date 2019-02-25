package br.com.ggdio.security.domain.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import br.com.ggdio.security.domain.exception.ExpiredRefreshWindowException;
import br.com.ggdio.specs.infrastructure.repository.TimestampRepository;

/**
 * A session indicates an authenticated user in the system
 * <p>
 * Token is a unique String value that identifies a session
 * 
 * @author Guilherme Dio
 *
 */
public class Session {
	
	public static final long SESSION_TIMEOUT = 3600000; // 1 hour
	public static final long REFRESH_TIMEOUT = 3600000L * 24; // 24 hours
	
	private static final long REFRESH_THRESHOLD = 600000; // 10 minutes

	private final AuthorizationType authType;
	private final String refreshToken;
	private String token;
	
	private final Set<Group> groups;
	private final User user;
	
	private final String userAgent;
	private final String host;
	private final String ip;
	
	private Boolean destroyed;
	
	private final LocalDateTime createdAt;
	
	private LocalDateTime lastToken;
	
	private LocalDateTime destroyedAt;
	private DestroyReason destroyReason;
	
	private final Set<App> apps;
	private final Set<Role> roles;

	private final TimestampRepository timestampRepository;
	
	//TODO: Token and RefreshToken generation should be handled internally by Session domain
	private Session(TimestampRepository timestampRepository, AuthorizationType authType, String refreshToken, String token, Set<Group> groups, User user, String userAgent, String host, String ip, LocalDateTime createdAt) {
		this.timestampRepository = timestampRepository;
		this.authType = authType;
		this.token = token;
		this.refreshToken = refreshToken;
		this.groups = new HashSet<>(groups);
		this.user = user;
		this.userAgent = userAgent;
		this.host = host;
		this.ip = ip;
		this.createdAt = createdAt;
		
		this.apps = new HashSet<>();
		this.roles = new HashSet<>();
		
		for (Group group : groups) {
			this.apps.addAll(group.getApps());
			this.roles.addAll(group.getRoles());
		}
	}
	
	public AuthorizationType getAuthType() {
		return authType;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}

	public Set<Group> getGroups() {
		return new HashSet<>(groups);
	}
	
	public Set<App> getApps() {
		return new HashSet<>(apps);
	}
	
	public Set<Role> getRoles() {
		return new HashSet<>(roles);
	}
	
	/**
	 * Validate user permissioning over an app
	 * @param key - App Key
	 * @return True or False
	 */
	public boolean canAccessApp(String key) {
		for (App app : apps) {
			if(app.getKey().equals(key)) return true;
		}
		return false;
	}
	
	/**
	 * Validate user permissioning over an action
	 * @param key - Action Key
	 * @return True or False
	 */
	public boolean canPerformAction(String key) {
		for (Role role : roles) {
			for (Action action : role.getActions()) {
				if(action.getKey().equals(key)) return true;
			}
		}
		return false;
	}

	public User getUser() {
		return user;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getHost() {
		return host;
	}

	public String getIp() {
		return ip;
	}
	
	public DestroyReason getDestroyReason() {
		return destroyReason;
	}
	
	public boolean isDestroyed() {
		return destroyed != null && destroyed.equals(Boolean.TRUE);
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public long getCreatedAtMilli() {
		return createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	public LocalDateTime getLastToken() {
		return lastToken;
	}
	
	public long getLastTokenMilli() {
		return lastToken.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public LocalDateTime getDestroyedAt() {
		return destroyedAt;
	}
	
	private void setLastToken(LocalDateTime lastToken) {
		this.lastToken = lastToken;
	}
	
	private void setDestroyReason(DestroyReason destroyReason) {
		this.destroyReason = destroyReason;
	}
	
	private void setDestroyedAt(LocalDateTime destroyedAt) {
		this.destroyedAt = destroyedAt;
	}
	
	private void setDestroyed(Boolean destroyed) {
		this.destroyed = destroyed;
	}
	
	/**
	 * Refreshes the current session token
	 * @param token - The new token value
	 */
	//TODO: Generate token automatically without depending on external services. 
	//      The parameterized 'token' may be a problem as it's not handled directly by Session.
	public void refreshToken(String token) {
		if(!isRefreshWindowActive()) {
			throw new ExpiredRefreshWindowException(token);
		}
		
		if(!isAlive(REFRESH_THRESHOLD)) { // only refresh expired sessions
			this.token = token;
			this.lastToken = currentTime();
		}
	}
	
	/**
	 * Validates if refresh window still active for renewing session token
	 * 
	 * @return True - If window is active
	 * 		   False - If window is closed
	 */
	public boolean isRefreshWindowActive() {
		return isDestroyed() ? false : getSessionDelta(getCreatedAtMilli(), REFRESH_TIMEOUT) > 0;
	}

	/**
	 * Completely destroy the current session (including refresh token)
	 * 
	 * @param reason - The destroying reason
	 */
	public void destroy(DestroyReason reason) {
		this.destroyReason = reason;
		this.destroyed = Boolean.TRUE;
		this.destroyedAt = currentTime();
	}

	/**
	 * Validates if current session token still alive
	 * 
	 * @return True - If token is alive
	 * 		   False - If token is expired (Must use refresh token to renew it)
	 */
	public boolean isAlive() {
		return isAlive(0);
	}
	
	private boolean isAlive(long threshold) {
		return isDestroyed() ? false : getSessionDelta(getLastTokenMilli(), SESSION_TIMEOUT) > threshold;
	}

	/**
	 * Calculate delta time for a give timeout
	 * 
	 * @param timeout - Timeout for delta calc
	 * @return delta time
	 */
	private long getSessionDelta(long last, long timeout) {
		LocalDateTime currentLDT = currentTime();
		
		long current = currentLDT.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long delta = timeout - (current - last);
		return delta;
	}
	
	/**
	 * Calculates the time to expire the session
	 * @return
	 */
	public long getExpiresIn() {
		long delta = getSessionDelta(getLastTokenMilli(), SESSION_TIMEOUT);
		
		return delta < 0 ? 0 : delta;
	}
	
	private LocalDateTime currentTime() {
		return Instant.ofEpochMilli(timestampRepository.currentTimestamp())
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
	
	public static NewSessionBuilder builder(TimestampRepository timestampRepository) {
		return new NewSessionBuilder(timestampRepository);
	}
	
	/**
	 * Builds a new Session
	 * 
	 * @author Guilherme Dio
	 *
	 */
	public static class NewSessionBuilder {
		
		private AuthorizationType authType;
		private String refreshToken;
		private String token;
		private Set<Group> groups = new HashSet<>();
		private User user;
		private String userAgent;
		private String host;
		private String ip;
		private Boolean destroyed;
		private LocalDateTime createdAt;
		private LocalDateTime lastToken;
		private LocalDateTime destroyedAt;
		private DestroyReason destroyReason;
		
		private final TimestampRepository timestampRepository;
		
		private NewSessionBuilder(TimestampRepository timestampRepository) {
			this.timestampRepository = timestampRepository;
		}
		
		public NewSessionBuilder authType(AuthorizationType authType) {
			this.authType = authType;
			return this;
		}
		
		public NewSessionBuilder token(String refreshToken, String token) {
			this.refreshToken = refreshToken;
			this.token = token;
			return this;
		}
		
		public NewSessionBuilder groups(Set<Group> groups) {
			this.groups.addAll(groups);
			return this;
		}
		
		public NewSessionBuilder groups(Group...groups) {
			this.groups.addAll(Arrays.asList(groups));
			return this;
		}
		
		public NewSessionBuilder user(User user) {
			this.user = user;
			return this;
		}
		
		public NewSessionBuilder userAgent(String userAgent) {
			this.userAgent = userAgent;
			return this;
		}
		
		public NewSessionBuilder host(String host) {
			this.host = host;
			return this;
		}
		
		public NewSessionBuilder ip(String ip) {
			this.ip = ip;
			return this;
		}
		
		public NewSessionBuilder createdAt(long millis) {
			return createdAt(Instant.ofEpochMilli(millis)
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime());
		}
		
		public NewSessionBuilder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}
		
		public NewSessionBuilder lastToken(LocalDateTime lastToken) {
			this.lastToken = lastToken;
			return this;
		}
		
		public NewSessionBuilder destroyed(DestroyReason destroyReason, LocalDateTime destroyedAt) {
			this.destroyed = Boolean.TRUE;
			this.destroyReason = destroyReason;
			this.destroyedAt = destroyedAt;
			return this;
		}
		
		public Session build() {
			Session session = new Session(timestampRepository, authType, refreshToken, token, groups, user, userAgent, host, ip, createdAt);
			session.setDestroyed(destroyed);
			session.setDestroyReason(destroyReason);
			session.setDestroyedAt(destroyedAt);
			session.setLastToken(lastToken);
			return session;
		}
		
	}
	
}