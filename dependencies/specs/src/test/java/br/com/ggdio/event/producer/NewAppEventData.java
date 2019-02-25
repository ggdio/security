package br.com.ggdio.event.producer;

import br.com.ggdio.specs.event.EventData;

public class NewAppEventData implements EventData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5129784011108732634L;
	
	private String appKey;
	private String appName;
	private String templateKey;
	
	public NewAppEventData() {
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

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}
	
	

}
