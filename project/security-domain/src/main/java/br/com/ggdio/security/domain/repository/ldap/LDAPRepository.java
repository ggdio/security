package br.com.ggdio.security.domain.repository.ldap;

import br.com.ggdio.security.domain.exception.AuthenticationException;
import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.ldap.LDAPData;

/**
 * Repository for LDAP access.
 * 
 * @see Group
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 14 Aug 2018
 * @since 1.0.0-RELEASE
 */
public interface LDAPRepository {

	public LDAPData authenticate(String username, String password, String domain) throws AuthenticationException;
	
}