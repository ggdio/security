package br.com.ggdio.security;

import br.com.ggdio.specs.event.EventData;

/**
 * Event data for App events
 * 
 * @author Guilherme Dio
 *
 */
public class AppEventData implements EventData {
	
	private static final long serialVersionUID = -98785393449539045L;
	
	private String appKey;
	private String appName;
	private String templateKey;
	private String userId;
	
	public AppEventData() {
		// TODO Auto-generated constructor stub
	}

	public String getAppKey() {
		return appKey;
	}

	public String getAppName() {
		return appName;
	}

	public String getTemplateKey() {
		return templateKey;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}