package br.com.ggdio.security.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages user sessions
 * 
 * @author Guilherme Dio
 *
 */
public class SessionManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(SessionManager.class);
	
	private RedisClient redis;
	private final HttpClient microservice;
	
	private static SessionManager singletonInstance;

	public SessionManager(DataSource dataSource) {
		try {
			if(dataSource.isSentinel()) {
				redis = new RedisClient(dataSource.getSentinelCluster(), dataSource.getSentinelList(), dataSource.getPassword());
				
			} else {
				redis = new RedisClient(dataSource.getRedisHost(), dataSource.getRedisPort(), dataSource.getPassword());
				
			}
			
		} catch(Exception e) {
			LOG.error("Failed to connect to " + ((dataSource.isSentinel()) ? "SENTINEL" : "REDIS"), e);
		}
		
		microservice = new HttpClient(dataSource.getMicroserviceEndpoint());
	}
	
	public Session get(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(header == null || header.isEmpty() || header.split("\\s").length < 2) return null;
		
		String token = header.split("\\s")[1];
		
		return get(token);
	}
	
	public Session get(HttpHeaders headers) {
		List<String> authorization = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
		if(authorization == null || authorization.isEmpty()) return null;
		
		String header = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
		if(header == null || header.isEmpty() || header.split("\\s").length < 2) return null;
		
		String token = header.split("\\s")[1];
		
		return get(token);
	}
	
	public Session get(String token) {
		Session session = getSessionFromRedis(token);
		
		if(session != null) {
			fill(session);
			return session;
		}
		
		session = microservice.validate(token);
		if(session != null) {
			setSessionOnRedis(session);
			fill(session);
			return session;
			
		} else {
			return null;
			
		}
	}
	
	private void fill(Session session) {
		session.setRedisClient(redis);
		session.setHttpClient(microservice);
	}
	
	private void setSessionOnRedis(Session session) {
		if(redis != null) {
			redis.set(session);
		}
	}

	private Session getSessionFromRedis(String token) {
		Session session = null;
		
		if(redis != null) {
			try {
				session = redis.get(token);
				
			} catch(Exception e) { 
				LOG.error("Problem retrieving session from redis.", e);
				
			}
		}
		
		return session;
	}
	
	public void setSingletonInstance() {
		SessionManager.singletonInstance = this;
	}
	
	public static SessionManager getSingletonInstance() {
		return singletonInstance;
	}
	
}