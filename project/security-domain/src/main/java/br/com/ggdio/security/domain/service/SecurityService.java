package br.com.ggdio.security.domain.service;

import java.util.HashSet;
import java.util.Set;

import br.com.ggdio.security.domain.exception.Error;
import br.com.ggdio.security.domain.model.App;
import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Session;
import br.com.ggdio.security.domain.model.Template;
import br.com.ggdio.security.domain.model.User;
import br.com.ggdio.security.domain.repository.AppRepository;
import br.com.ggdio.security.domain.repository.SessionRepository;
import br.com.ggdio.security.domain.repository.TemplateRepository;
import br.com.ggdio.security.domain.repository.UserRepository;
import br.com.ggdio.specs.common.ComponentRegistry;
import br.com.ggdio.specs.domain.exception.DomainException;

/**
 * Cross domain service for security issues
 * 
 * @author Guilherme Dio
 *
 */
public class SecurityService {

	private final ComponentRegistry registry;
	
	public SecurityService(ComponentRegistry registry) {
		this.registry = registry;
	}
	
	public App secureApp(String appKey, String templateKey) {
		return secureApp(appKey, null, templateKey, null);
	}
	
	public App secureApp(String appKey, String appName, String templateKey, String userKey) {
		try {
			if(appKey == null) throw new DomainException("appKey cannot be null.");
			checkDuplicate(appKey); // KEY is unique
			
			TemplateRepository templateRepo = registry.lookup(TemplateRepository.class);
			AppRepository appRepo = registry.lookup(AppRepository.class);
			UserRepository userRepo = registry.lookup(UserRepository.class);
			SessionRepository sessionRepo = registry.lookup(SessionRepository.class);
			
			Set<Group> defaultGroups = new HashSet<>(); // groups to be related with
			
			Template template = templateRepo.findByKey(templateKey);
			if(template != null) { // relate template groups with the new app
				defaultGroups.addAll(template.getGroups());
			}
			
			if(userKey != null && !userKey.isEmpty()) { // gather user groups and relate with the new app
				User user = userRepo.findByKey(userKey);
				if(user == null) {
					Error error = Error.USER_DOESNT_EXISTS;
					throw new DomainException(error.getCode(), error.getMessage(userKey), null);
				}
				
				Session session = sessionRepo.findByUser(user.getId());
				defaultGroups.addAll(session.getGroups());
			}
			
			App app = new App(null, appKey, defaultGroups);
			if(appName == null) {
				appName = appKey.toUpperCase();
			}
			app.setDescription(appName);
			
			appRepo.save(app); // finally save
			
			return app; // contains auto-generated ID
			
		} catch(DomainException e) {
			throw e;
			
		} catch(Exception e) {
			throw new DomainException("Could not manage new app due to '" + e.getMessage() + "'", e);
			
		}
	}

	private void checkDuplicate(String appKey) {
		App app = registry.lookup(AppRepository.class).findByKey(appKey);
		if(app != null) {
			throw new DomainException("App '"+appKey+"' already exists.");
		}
	}
	
}
