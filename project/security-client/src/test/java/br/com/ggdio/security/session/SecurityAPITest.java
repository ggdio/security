package br.com.ggdio.security.session;

import br.com.ggdio.security.session.Role;
import br.com.ggdio.security.session.SecurityAPI;

public class SecurityAPITest {

	public static void main(String[] args) {
		SecurityAPI api = new SecurityAPI("http://localhost:8080/api/v1/");
		Role role = api.getRole("admin");
		System.out.println(role);
	}
	
}