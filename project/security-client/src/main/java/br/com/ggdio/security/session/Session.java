package br.com.ggdio.security.session;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A session represents the state of a current logged in user
 * 
 * @author Guilherme Dio
 *
 */
public class Session {
	
	private String token;
	private String tokenType;
	
	private AtomicBoolean destroyed = new AtomicBoolean(false);
	
	private long expiresIn;
	private final long refTimestamp;
	
	private String userId;
	
	private final Set<String> apps;
	private final Set<String> roles;
	private final Set<String> actions;
	
	private HttpClient httpClient;
	private RedisClient redisClient;
	
	private static final long EXPIRATION_THRESHOLD = 30000;
	
	private static final ExecutorService executor = Executors.newFixedThreadPool(10);
	
	Session() {
		this(null, null);
	}
	
	Session(RedisClient redisClient, HttpClient httpClient) {
		this.refTimestamp = System.currentTimeMillis();
		
		this.redisClient = redisClient;
		this.httpClient = httpClient;
		
		this.apps = new HashSet<>();
		this.roles = new HashSet<>();
		this.actions = new HashSet<>();
	}
	
	/**
	 * Invalidates the current session
	 */
	public void destroy() {
		boolean alreadyDestroyed = !destroyed.compareAndSet(false, true);
		if(alreadyDestroyed) return;
		
		executor.submit(() -> redisClient.remove(token));
		executor.submit(() -> httpClient.destroy(token));
	}
	
	/**
	 * Check app permission for current user
	 * 
	 * @param appKey - The application KEY
	 * @return true - if permitted<br>
	 *         false - if forbidden
	 */
	public boolean hasApp(String appKey) {
		return apps.contains(appKey);
	}
	
	/**
	 * Check if current user is member of a given role
	 * @param roleKey - The role KEY
	 * @return true - if is a member<br>
	 *         false - if is not a member
	 */
	public boolean isMemberOf(String roleKey) {
		return roles.contains(roleKey);
	}
	
	/**
	 * Check app permission for current user
	 * 
	 * @param actionKey - The action KEY
	 * @return true - if permitted<br>
	 *         false - if forbidden
	 */
	public boolean hasPermission(String actionKey) {
		return actions.contains(actionKey);
	}
	
	/**
	 * Validate if session still alive<br>
	 * Fallbacks:
	 * 1st - Try redis<br>
	 * 2nd - Try microservice<br>
	 * 3rd - Use 'expiresIn' field<br>
	 * 
	 * @return true - if alive<br>
	 *         false - if expired or destroyed
	 */
	public boolean isAlive() {
		
		try {
			return redisClient.exists(token);
			
		} catch(Exception e1) {
			try {
				return httpClient.isValid(token);
				
			} catch(Exception e2) {
				long expiresIn = getExpiresIn();
				return expiresIn > EXPIRATION_THRESHOLD;
				
			}
		}
	}

	public String getToken() {
		return token;
	}

	public String getTokenType() {
		return tokenType;
	}

	public long getExpiresIn() {
		long expiresIn =  this.expiresIn - (System.currentTimeMillis() - refTimestamp);
		return expiresIn > 0 ? expiresIn : 0;
	}
	
	long getRefTimestamp() {
		return refTimestamp;
	}

	public String getUserId() {
		return userId;
	}

	public Set<String> getApps() {
		return new HashSet<>(apps);
	}

	public Set<String> getRoles() {
		return new HashSet<>(roles);
	}
	
	public Set<String> getActions() {
		return new HashSet<>(actions);
	}
	
	void setToken(String token) {
		this.token = token;
	}

	void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	void setUserId(String userId) {
		this.userId = userId;
	}
	
	void addApp(String app) {
		this.apps.add(app);
	}
	
	void addApps(Collection<String> apps) {
		this.apps.addAll(apps);
	}
	
	void addRole(String role) {
		this.roles.add(role);
	}
	
	void addRoles(Collection<String> roles) {
		this.roles.addAll(roles);
	}
	
	void addAction(String action) {
		this.actions.add(action);
	}
	
	void addActions(Collection<String> actions) {
		this.actions.addAll(actions);
	}
	
	void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

	@Override
	public String toString() {
		return "Session [token=" + token + ", tokenType=" + tokenType + ", destroyed=" + destroyed + ", expiresIn="
				+ getExpiresIn() + ", refTimestamp=" + refTimestamp + ", userId=" + userId + ", apps=" + apps + ", roles="
				+ roles + ", actions=" + actions + ", httpClient=" + httpClient
				+ ", redisClient=" + redisClient + "]";
	}
	
}