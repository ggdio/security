package br.com.ggdio;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LDAPClientTest {
	
	public static void main(String[] args) throws NamingException {
		LDAPClientTest client = new LDAPClientTest();
		Attributes attrs = client.userIsAuthorized("", "", "BSBR");
		
		Attribute attrSama = attrs.get("sAMAccountName");
		String sama = attrSama == null ? null : ((String)attrSama.get()).toLowerCase();
		
		Attribute attrDisplayName = attrs.get("displayName");
		String displayName = attrDisplayName == null ? null : ((String)attrDisplayName.get());
		
		Attribute attrMail = attrs.get("mail");
		String mail = attrMail == null ? null : ((String)attrMail.get()).toLowerCase();
		
		Attribute attrMemberOf = attrs.get("memberOf");
		NamingEnumeration<?> groups = attrMemberOf == null ? null : attrMemberOf.getAll();
		
		List<String> memberOf = new ArrayList<>(); 
		
		while(groups.hasMore()) {
			String group = (String) groups.next();
			String[] groupSplitted = group.split(",");
			group = groupSplitted[0].replace("CN=", "");
			memberOf.add(group);
		}
		
		System.out.println(sama);
		System.out.println(displayName);
		System.out.println(mail);
		System.out.println(memberOf);
		
		NamingEnumeration<String> iDs = attrs.getIDs();
		while(iDs.hasMoreElements()) {
			String next = iDs.nextElement();
			System.out.println(next);
		}
		
	}

	private Attributes userIsAuthorized(String username, String password, String domain) {

		String baseDn = "";

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		StringBuilder url = new StringBuilder("ldap://");
		if ("PRBBR".equalsIgnoreCase(domain)) {
			url.append("ldapprbbr.");
			baseDn = "DC=prbbr,DC=produbanbr,DC=corp";
		} else {
			baseDn = "DC=bs,DC=br,DC=bsch";
		}
		url.append("bs.br.bsch:389");
		env.put(Context.PROVIDER_URL, url.toString());
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, domain + "\\" + username);
		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
			// Trying to connect
			DirContext ctx = new InitialDirContext(env);

			// Search logged user and get its attributes
			SearchControls sctls = new SearchControls();
			String retatts[] = { "mail", "displayName", "sAMAccountName", "memberOf" };
			sctls.setReturningAttributes(retatts);
			sctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String srchfilter = "(&(objectClass=user)(mail=*) (sAMAccountName=" + username + "))";

			NamingEnumeration<SearchResult> result = ctx.search(baseDn, srchfilter, sctls);

			if (!result.hasMoreElements())
				throw new Exception("Não foi encontrado registro no AD para logar esse usuário");

			Attributes attrs = result.nextElement().getAttributes();

			ctx.close();

			return attrs;

		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

}
