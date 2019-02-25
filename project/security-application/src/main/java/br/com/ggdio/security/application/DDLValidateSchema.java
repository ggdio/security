package br.com.ggdio.security.application;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@SpringBootApplication
public class DDLValidateSchema {
	
	private static final Logger LOG = LoggerFactory.getLogger(DDLValidateSchema.class);
	
	private static final String UNIT = "validate";

	public static void main(String[] args) {
		LOG.info("INITIALIZING DDL GENERATION AND SCHEMA VALIDATION...");
		
		ConfigurableApplicationContext ctx = SpringApplication.run(DDLValidateSchema.class, args);
		
		LOG.info("DDL GENERATION AND SCHEMA VALIDATION COMPLETED SUCCESSFULLY !");
		
        ctx.close();
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
	
}
