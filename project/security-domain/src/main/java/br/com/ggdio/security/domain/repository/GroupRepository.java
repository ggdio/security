package br.com.ggdio.security.domain.repository;

import java.util.List;
import java.util.Set;

import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.specs.infrastructure.repository.Transactional;

/**
 * {@link Group} Repository.
 * 
 * @see Group
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 14 Aug 2018
 * @since 1.0.0-RELEASE
 */
public interface GroupRepository extends Transactional {

	public void save(Group group);
	
	public Set<Group> findByKeys(List<String> keys);

	public Set<Group> findByRole(Long id);
	
}