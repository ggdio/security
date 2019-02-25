package br.com.ggdio.security.application.rest.view;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.ggdio.security.domain.dto.Login;
import br.com.ggdio.specs.application.view.ItemView;

@XmlRootElement(name="login")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginView implements ItemView<Login> {
	
	@XmlElement(name="domain")
	private String domain;

	@XmlElement(name="user")
	@NotNull(message = "User cannot be null")
	@NotBlank(message = "User cannot be blank")
	private String user;
	
	@XmlElement(name="password")
	@NotNull(message = "Password cannot be null")
	@NotBlank(message = "Password cannot be blank")
	private String password;
	
	public String getDomain() {
		return domain;
	}

	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public Login unwrap() {
		return new Login(domain, user, password);
	}
	
}