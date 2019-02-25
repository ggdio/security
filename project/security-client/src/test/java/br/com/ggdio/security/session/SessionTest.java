package br.com.ggdio.security.session;

import br.com.ggdio.security.session.DataSource;
import br.com.ggdio.security.session.Session;
import br.com.ggdio.security.session.SessionManager;

public class SessionTest {
	
	private static final String TOKEN = "XPTO-123";

	public static void main(String[] args) {
		DataSource dataSource = new DataSource("http://localhost:8080/api/v1/", "localhost", 6379, null);
		SessionManager sessionManager = new SessionManager(dataSource);
		Session session = sessionManager.get(TOKEN);
		
		System.out.println(session.getExpiresIn());
	}
	
}