package br.com.ggdio.security.domain.model;

/**
 * Action Domain Model.
 * <p>
 * It defines a system interaction
 * (eg.: button click, screen visualization, backend call, etc..)
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 21 Jun 2018
 * @since 1.0.0-RELEASE
 */
public class Action {

	private final Long id;
	private final String key;
	
	private String description;
	
	public Action(String key) {
		this(null, key);
	}

	public Action(Long id, String key) {
		this.id = id;
		this.key = key;
	}

	public Long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}