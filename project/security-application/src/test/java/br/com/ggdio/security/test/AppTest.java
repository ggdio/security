package br.com.ggdio.security.test;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.ggdio.security.common.BeanComponentRegistry;
import br.com.ggdio.security.domain.service.LoginService;
import br.com.ggdio.security.domain.service.SecurityService;

/**
 * App Bootstrapper Test
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 21 Aug 2018
 * @since 1.0.0-RELEASE
 */
@Configuration
@ComponentScan({"br.com.ggdio.security", "br.com.ggdio.specs.jaxrs"})
public class AppTest {
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
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

}