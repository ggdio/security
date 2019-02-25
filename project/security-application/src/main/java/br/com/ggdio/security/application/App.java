package br.com.ggdio.security.application;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.ggdio.security.application.event.AppEventListener;
import br.com.ggdio.security.common.BeanComponentRegistry;
import br.com.ggdio.security.domain.service.LoginService;
import br.com.ggdio.security.domain.service.SecurityService;
import br.com.ggdio.specs.event.EventReceiver;

/**
 * App Bootstrapper
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 21 Jun 2018
 * @since 1.0.0-RELEASE
 */
@Configuration
@ComponentScan({ "br.com.ggdio.security", "br.com.ggdio.specs.jaxrs" })
@SpringBootApplication
public class App {

	private static final String UNIT = "default";

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setPersistenceUnitName(UNIT);
		return emf;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
	
	@Bean
	@Autowired
	public SecurityService securityService(BeanComponentRegistry registry) {
		return new SecurityService(registry);
	}

	@Bean
	@Autowired
	public LoginService loginService(BeanComponentRegistry registry) {
		return new LoginService(registry);
	}

	@Bean
	@Autowired
	public EventReceiver eventReceiver(Environment env, SecurityService service) {
		String host = env.getRequiredProperty("event.receiver.host", String.class);
		int port = env.getProperty("event.receiver.port", int.class, 0);
		String username = env.getProperty("event.receiver.username", String.class, null);
		String password = env.getProperty("event.receiver.password", String.class, null);
		
		if("null".equals(username)) username = null;
		
		EventReceiver receiver = new EventReceiver(host, port, username, password);
		receiver.declareEventId(AppEventListener.EVENT_ID, AppEventListener.QUEUE);
		receiver.registerListener(new AppEventListener(service));
		
		return receiver;
	}
	
	

}