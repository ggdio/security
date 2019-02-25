package br.com.ggdio.security.domain.model.ldap;

import java.util.ArrayList;
import java.util.List;

/**
 * LDAP Search Result Data
 * 
 * @author Guilherme Dio
 *
 */
public class LDAPData {

	private final String sAMAccountName;
	private final String displayName;
	private final String mail;
	private final List<String> groups;
	
	public LDAPData(String sAMAccountName, String displayName, String mail, List<String> groups) {
		this.sAMAccountName = sAMAccountName;
		this.displayName = displayName;
		this.mail = mail;
		this.groups = new ArrayList<>(groups);
	}

	public String getsAMAccountName() {
		return sAMAccountName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getMail() {
		return mail;
	}

	public List<String> getGroups() {
		return new ArrayList<>(groups);
	}
	
}