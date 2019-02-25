package br.com.ggdio.security.session;

/**
 * DataSource provider for {@link SessionManager}
 * 
 * @author Guilherme Dio
 *
 */
public class DataSource {

	private final String microserviceEndpoint;
	
	private final String redisHost;
	private final int redisPort;
	
	private final String sentinelCluster;
	private final String sentinelList;
	
	private final String password;
	
	private final boolean sentinel;

	public DataSource(String microserviceEndpoint, String redisHost, int redisPort, String password) {
		this.sentinel = false;
		
		this.microserviceEndpoint = microserviceEndpoint;
		this.redisHost = redisHost;
		this.redisPort = redisPort;
		
		this.password = password;
		
		this.sentinelCluster = null;
		this.sentinelList = null;
	}

	public DataSource(String microserviceEndpoint, String sentinelCluster, String sentinelList, String password) {
		this.sentinel = true;
		
		this.microserviceEndpoint = microserviceEndpoint;
		
		this.sentinelCluster = sentinelCluster;
		this.sentinelList = sentinelList;
		
		this.password = password;
		
		this.redisHost = null;
		this.redisPort = -1;
	}

	public String getMicroserviceEndpoint() {
		return microserviceEndpoint;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public String getSentinelCluster() {
		return sentinelCluster;
	}

	public String getSentinelList() {
		return sentinelList;
	}

	public String getPassword() {
		return password;
	}

	public boolean isSentinel() {
		return sentinel;
	}
	
	
}