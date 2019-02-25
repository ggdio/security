package br.com.ggdio.security.domain.repository;

import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Template;

/**
 * {@link Template} Repository.
 * 
 * @see Group
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 20 Aug 2018
 * @since 1.0.0-RELEASE
 */
public interface TemplateRepository {

	public void save(Template template);
	
	public Template findByKey(String key);
	
}