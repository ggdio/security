package br.com.ggdio.security.application.rest.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.ggdio.security.domain.model.Session;

@XmlRootElement(name="session")
@XmlAccessorType(XmlAccessType.FIELD)
public class SessionView {

	@XmlElement(name="access_token")
	private String accessToken;
	
	@XmlElement(name="refresh_token")
	private String refreshToken;
	
	@XmlElement(name="token_type")
	private String tokenType;
	
	@XmlElement(name="expires_in")
	private long expiresIn;
	
	@XmlElement(name="user_id")
	private String userId;
	
	@XmlElement(name="name")
	private String name;
	
	@XmlElement(name="scope")
	private SessionScopeView scope;
	
	public SessionView() {
		
	}
	
	public SessionView(Session session) {
		this.accessToken = session.getToken();
		this.refreshToken = session.getRefreshToken();
		this.tokenType = session.getAuthType().getScheme();
		this.expiresIn = session.getExpiresIn();
		this.userId = session.getUser().getKey();
		this.name = session.getUser().getName();
		
		this.scope = new SessionScopeView(session);
	}

	public String getAccessToken() {
		return accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public String getUserId() {
		return userId;
	}
	
	public String getName() {
		return name;
	}

	public SessionScopeView getScope() {
		return scope;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setScope(SessionScopeView scope) {
		this.scope = scope;
	}
	
}