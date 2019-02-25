package br.com.ggdio.security.infrastructure.repository;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.repository.GroupRepository;
import br.com.ggdio.security.infrastructure.entity.GroupEntity;
import br.com.ggdio.specs.infrastructure.repository.JPARepository;

@Repository
public class DefaultGroupRepository extends JPARepository<Group, GroupEntity> implements GroupRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public void save(Group group) {
		super.save(group);
	}

	@Override
	public Set<Group> findByKeys(List<String> keys) {
		String query = "SELECT e "
					 + "FROM GroupEntity e "
					 + "WHERE e.key in :keys";
		return super.query(query, "keys", keys);
	}
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Set<Group> findByRole(Long id) {
		String query = "SELECT g "
				 	 + "FROM GroupEntity g "
				 	 + "JOIN g.roles r ON r.id = :roleId";
		return super.query(query, "roleId", id);
	}

}