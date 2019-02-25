package br.com.ggdio.security.infrastructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.ggdio.security.infrastructure.entity.SysdateEntity;
import br.com.ggdio.specs.infrastructure.repository.TimestampRepository;

@Repository
public class DefaultTimestampRepository implements TimestampRepository {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public long currentTimestamp() {
		Query query = em.createNativeQuery("SELECT SYSDATE() AS DT_SYSDATE FROM DUAL", SysdateEntity.class);
		SysdateEntity dateItem = (SysdateEntity) query.getSingleResult();
		return dateItem.getTimestamp();
	}

}