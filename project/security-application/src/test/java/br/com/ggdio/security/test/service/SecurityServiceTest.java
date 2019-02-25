package br.com.ggdio.security.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ggdio.security.test.AppTest;
import br.com.ggdio.security.domain.model.Action;
import br.com.ggdio.security.domain.model.App;
import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.security.domain.model.Template;
import br.com.ggdio.security.domain.repository.ActionRepository;
import br.com.ggdio.security.domain.repository.AppRepository;
import br.com.ggdio.security.domain.repository.GroupRepository;
import br.com.ggdio.security.domain.repository.RoleRepository;
import br.com.ggdio.security.domain.repository.TemplateRepository;
import br.com.ggdio.security.domain.service.SecurityService;

//@DataJpaTest
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes= {AppTest.class})
public class SecurityServiceTest {
	
//	@Autowired
//	private SecurityService service;
//	
//	@Autowired
//	private AppRepository appRepository;
//	
//	@Autowired
//	private TemplateRepository templateRepository;
//	
//	@Autowired
//	private GroupRepository groupRepository;
//	
//	@Autowired
//	private RoleRepository roleRepository;
//	
//	@Autowired
//	private ActionRepository actionRepository;
//	
//	@Before
//	@SuppressWarnings("serial")
//	public void mountData() {
//		Group group = new Group("SQUADS");
//		Role role;
//		
//		// ROLE: ADMIN
//		role = new Role(null, "admin", new HashSet<Action>() {{
//			add(new Action("appx.screen1.btnadd.click"));
//			add(new Action("appx.screen2.btnremove.click"));
//			add(new Action("appx.screen2.view"));
//		}});
//		role.getActions().forEach(actionRepository::save);
//		roleRepository.save(role);
//		group.addRole(role);
//		
//		// ROLE: OPERATOR
//		role = new Role(null, "operator", new HashSet<Action>() {{
//			add(new Action("appx.screen3.btnadd.click"));
//		}});
//		role.getActions().forEach(actionRepository::save);
//		roleRepository.save(role);
//		group.addRole(role);
//		
//		// SAVE GROUP
//		groupRepository.save(group);
//		
//		// TEMPLATE: DEFAULT
//		Template template = new Template("default");
//		template.addGroup(group);
//		template.setDescription("Default Template");
//		templateRepository.save(template);
//	}
//	
//	@Test
//	public void testSecureNewApp() {
//		App app = service.secureApp("appx", "default");
//		
//		assertNotNull("App should have been created", app);
//		assertNotNull("App should have been persisted", app.getId());
//		
//		App findByKey = appRepository.findByKey("appx");
//		assertNotNull("App should have been persisted with the parameterized key", findByKey);
//		assertEquals("App should maintain the original parameterized name", "App X", findByKey.getDescription());
//	}
	
}