package br.com.ggdio.security.application;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.ggdio.security.domain.model.Action;
import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.security.domain.model.Template;
import br.com.ggdio.security.domain.repository.ActionRepository;
import br.com.ggdio.security.domain.repository.GroupRepository;
import br.com.ggdio.security.domain.repository.RoleRepository;
import br.com.ggdio.security.domain.repository.TemplateRepository;

@Configuration
@ComponentScan({"br.com.ggdio.security.common", "br.com.ggdio.security.infrastructure"})
@SpringBootApplication
public class DMLGenerateBaseData {
	
	private static final Logger LOG = LoggerFactory.getLogger(DMLGenerateBaseData.class);
	
	private static final String UNIT = "default";

	public static void main(String[] args) {
		LOG.info("INITIALIZING DML BASE DATA GENERATION...");
		
		ConfigurableApplicationContext ctx = SpringApplication.run(DMLGenerateBaseData.class, args);
		
		createGroups(ctx);
		
		LOG.info("DML BASE DATA GENERATION COMPLETED SUCCESSFULLY !");
		
        ctx.close();
	}
	
	private static void createGroups(ConfigurableApplicationContext ctx) {
		/*
		 * ADMIN Groups
		 */
		checkAndCreateGroup(ctx, "admin", "ADMIN", "admin", "Admin", "default", "Default Template");
		
		/*
		 * Common User
		 */
		checkAndCreateGroup(ctx, "user", "USER", "user", "User", null, null);
		
		checkAndCreateActions(ctx, "admin", Arrays.asList("frontend.xpto.create",
				  "frontend.xpto.edit",
				  "frontend.xpto.delete"));
		
		checkAndCreateActions(ctx, "user", Arrays.asList("frontend.xpto.create",
				  "frontend.xpto.edit"));
		
		if("true".equals(System.getProperty("dev", "false"))) {
			checkAndCreateGroup(ctx, "admin", "ADMIN", "admin", "Admin", "default", "Default Template");
			checkAndCreateGroup(ctx, "user", "User", "user", "User", null, null);
		}
	}
	
	private static void checkAndCreateActions(ConfigurableApplicationContext ctx, String role, List<String> actions) {
		ActionRepository actionRepository = ctx.getBean(ActionRepository.class);
		RoleRepository roleRepository = ctx.getBean(RoleRepository.class);
		Role roleData = roleRepository.findByKey(role);
		
		Set<Action> allActions = actionRepository.findAll();
		Map<String, Action> map = new HashMap<>(allActions.size());
		for (Action action : allActions) {
			map.put(action.getKey(), action);
		}
		
		for (String action : actions) {
			Action actionData = null;
			if(!map.containsKey(action)) {
				actionData = new Action(action);
				actionRepository.save(actionData);
			} else {
				actionData = map.get(action);
			}
			roleData.addAction(actionData);
		}
		roleRepository.save(roleData);
	}

	private static void checkAndCreateGroup(ConfigurableApplicationContext ctx, String groupKey, String groupDesc, String roleKey, String roleDesc, String templateKey, String templateDesc) {
		GroupRepository groupRepository = ctx.getBean(GroupRepository.class);
		RoleRepository roleRepository = ctx.getBean(RoleRepository.class);
		TemplateRepository templateRepository = ctx.getBean(TemplateRepository.class);
		
		Group group;
		Set<Group> result = groupRepository.findByKeys(Arrays.asList(groupKey));
		if(result == null || result.isEmpty()) {
			group = new Group(groupKey);
			group.setDescription(groupDesc);
			groupRepository.save(group);
		} else {
			group = result.iterator().next();
		}
		
		Role role = roleRepository.findByKey(roleKey);
		if(role == null) {
			role = new Role(null, roleKey);
			role.setDescription(roleDesc);
			roleRepository.save(role);
		}
		
		if(!group.containsRole(role.getKey())) {
			group.addRole(role);
			groupRepository.save(group);
		}
		
		if(templateKey != null) {
			Template template = templateRepository.findByKey(templateKey);
			if(template == null) {
				template = new Template(templateKey);
				template.addGroup(group);
				template.setDescription(templateDesc);
				
			} else {
				template.addGroup(group);
				
			}
			
			templateRepository.save(template);
		}
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