package br.com.ggdio.security.infrastructure.repository.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.stereotype.Repository;

import br.com.ggdio.security.domain.exception.AuthenticationException;
import br.com.ggdio.security.domain.model.ldap.LDAPData;
import br.com.ggdio.security.domain.repository.ldap.LDAPRepository;
import br.com.ggdio.specs.infrastructure.exception.InfrastructureException;

@Repository
public class DefaultLDAPRepository implements LDAPRepository {
	private static String retatts[] = { "mail", "displayName", "sAMAccountName", "memberOf" };

	@Override
	public LDAPData authenticate(String username, String password, String domain) throws AuthenticationException {
		LDAPData devUser = authenticateDev(username, password);
		if (devUser != null)
			return devUser;

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

		StringBuilder url = new StringBuilder("ldap://");

		String baseDn = "";
		if ("CUSTOMDOMAIN".equalsIgnoreCase(domain)) {
			url.append("ldapprbbr.");
			baseDn = "DC=ssub2,DC=ssub1,DC=mmain";
		} else {
			domain = "DEFAULTDOMAIN";
			baseDn = "DC=sub2,DC=sub1,DC=main";
		}
		url.append("sub2.sub1.main:389");
		env.put(Context.PROVIDER_URL, url.toString());
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, domain + "\\" + username);
		env.put(Context.SECURITY_CREDENTIALS, password);

		DirContext ctx = null;
		try {
			// Trying to connect
			ctx = new InitialDirContext(env);

			// Search logged user and get its attributes
			SearchControls sctls = new SearchControls();
			sctls.setReturningAttributes(retatts);
			sctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String srchfilter = "(&(objectClass=user)(mail=*) (sAMAccountName={0}))";

			NamingEnumeration<SearchResult> result = ctx.search(baseDn, srchfilter, new String[] {escapeFilter(username)}, sctls);

			if (!result.hasMoreElements())
				return null;

			Attributes attrs = result.nextElement().getAttributes();

			ctx.close();

			return mount(attrs);

		} catch (javax.naming.AuthenticationException e) {
			return null;

		} catch (Exception e) {
			throw new InfrastructureException("LDAP Authentication failed", e);

		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private LDAPData authenticateDev(String username, String accessKey) {
		if ("true".equals(System.getProperty("dev", "false"))) {
			if ("admin".equals(username) && "##admin##".equals(accessKey)) {
				return new LDAPData("admin", "Admin", "admin@ggdio.com.br", Arrays.asList("admin"));
			} else if ("user".equals(username) && "##user##".equals(accessKey)) {
				return new LDAPData("user", "User", "user@ggdio.com.br", Arrays.asList("user"));
			}
		}
		return null;
	}

	public LDAPData mount(Attributes attrs) {
		try {
			Attribute attrSama = attrs.get("sAMAccountName");
			String sama = attrSama == null ? null : ((String) attrSama.get()).toLowerCase();

			Attribute attrDisplayName = attrs.get("displayName");
			String displayName = attrDisplayName == null ? null : ((String) attrDisplayName.get());

			Attribute attrMail = attrs.get("mail");
			String mail = attrMail == null ? null : ((String) attrMail.get()).toLowerCase();

			Attribute attrMemberOf = attrs.get("memberOf");
			NamingEnumeration<?> groups = attrMemberOf == null ? null : attrMemberOf.getAll();

			// cria a lista inicial com 5 elementos, como quantidade media de grupos por
			// usuario, para reduzir a quantidade de realocacoes
			List<String> memberOf = new ArrayList<>(5);

			if (groups != null) {
				while (groups.hasMore()) {
					String group = (String) groups.next();
					String[] groupSplitted = group.split(",");
					group = groupSplitted[0].replace("CN=", "");
					memberOf.add(group);
				}
			}

			return new LDAPData(sama, displayName, mail, memberOf);

		} catch (Exception e) {
			throw new InfrastructureException("Could not mount LDAPData due to '" + e.getMessage() + "'", e);

		}

	}

	public static String escapeFilter(String filter) {
		if ( filter == null ) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		if ((filter.length() > 0) && ((filter.charAt(0) == ' ') || (filter.charAt(0) == '#'))) {
			sb.append('\\'); // add the leading backslash if needed
		}
		for (int i = 0; i < filter.length(); i++) {
			char curChar = filter.charAt(i);
			switch (curChar) {
			case '\\':
				sb.append("\\\\");
				break;
			case ',':
				sb.append("\\,");
				break;
			case '+':
				sb.append("\\+");
				break;
			case '"':
				sb.append("\\\"");
				break;
			case '<':
				sb.append("\\<");
				break;
			case '>':
				sb.append("\\>");
				break;
			case ';':
				sb.append("\\;");
				break;
			default:
				sb.append(curChar);
			}
		}
		if ((filter.length() > 1) && (filter.charAt(filter.length() - 1) == ' ')) {
			sb.insert(sb.length() - 1, '\\'); // add the trailing backslash if needed
		}
		return sb.toString();
	}
}
