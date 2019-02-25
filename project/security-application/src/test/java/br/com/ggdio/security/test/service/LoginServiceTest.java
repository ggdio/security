package br.com.ggdio.security.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.security.domain.model.Session;
import br.com.ggdio.security.domain.model.Template;
import br.com.ggdio.security.domain.repository.ActionRepository;
import br.com.ggdio.security.domain.repository.GroupRepository;
import br.com.ggdio.security.domain.repository.RoleRepository;
import br.com.ggdio.security.domain.repository.TemplateRepository;
import br.com.ggdio.security.domain.service.LoginService;
import br.com.ggdio.security.domain.service.SecurityService;

//@DataJpaTest
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes= {AppTest.class})
public class LoginServiceTest {
	
//	@Autowired
//	private LoginService loginService;
//	
//	@Autowired
//	private SecurityService securityService;
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
//		Group group = new Group("GAPL_SUDO_BDATA_CLOUD");
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
//		
//		// APP 4 TEST
//		securityService.secureApp("appx", "default");
//	}
//	
//	@Test
//	public void testLogin() {
//		Session session = loginService.login(System.getProperty("username"), System.getProperty("password"));
//		
//		assertNotNull("Session should never be null", session);
//		assertTrue("Should have a related group", session.getGroups().size() == 1);
//		assertEquals("GAPL_SUDO_BDATA_CLOUD", session.getGroups().iterator().next().getKey());
//		assertTrue("Should have a related APP", session.getGroups().iterator().next().getApps().size() == 1);
//		assertEquals("Related APP should be 'APP X'", "appx", session.getGroups().iterator().next().getApps().iterator().next().getKey());
//	}
//	
//	@Test
//	public void testTokenReuse() {
//		Session session1 = loginService.login(System.getProperty("username"), System.getProperty("password"));
//		Session session2 = loginService.login(System.getProperty("username"), System.getProperty("password"));
//		
//		assertEquals("Tokens should be equal for an active session", session1.getToken(), session2.getToken());
//	}
	
}