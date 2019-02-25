package br.com.ggdio.security.domain.dto;

/**
 * DTO for Login Data
 * @author n884420
 *
 */
public class Login {

	private final String domain;
	
	private final String username;
	private final String password;
	
	private String userAgent;
	private String host;
	private String ip;
	
	public Login(String domain, String username, String password) {
		if(username == null) throw new NullPointerException("USERNAME cannot be null");
		if(password == null) throw new NullPointerException("PASSWORD cannot be null");
		
		if(username.contains("\\")) {
			int indexOf = username.indexOf('\\');
			
			domain = username.substring(0, indexOf);
			username = username.substring(indexOf + 1);
			
		}
		
		this.domain = domain;
		this.username = username;
		this.password = password;
	}

	public String getDomain() {
		return domain;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
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

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "Login [domain=" + domain + ", username=" + username + ", password=" + password + ", userAgent="
				+ userAgent + ", host=" + host + ", ip=" + ip + "]";
	}
	
}