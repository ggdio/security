package br.com.ggdio.security.test.repository;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ggdio.security.test.AppTest;
import br.com.ggdio.security.domain.model.Action;
import br.com.ggdio.security.domain.repository.ActionRepository;

//@DataJpaTest
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes= {AppTest.class})
public class OrderByTest {

//	@Autowired
//	private ActionRepository actionRepository;
//	
//	@Before
//	public void mountData() {
//		actionRepository.save(new Action("x.screen1.btnadd.click"));
//		actionRepository.save(new Action("c.screen1.btnadd.click"));
//		actionRepository.save(new Action("y.screen1.btnadd.click"));
//		actionRepository.save(new Action("b.screen1.btnadd.click"));
//		actionRepository.save(new Action("z.screen1.btnadd.click"));
//		actionRepository.save(new Action("t.screen1.btnadd.click"));
//		actionRepository.save(new Action("a.screen1.btnadd.click"));
//	}
//	
//	@Test
//	public void testOrderByKey() throws InterruptedException {
//		Set<Action> findAll = actionRepository.findAll();
//		
//		assertEquals("The first element key should be 'a.screen1.btnadd.click' ", "a.screen1.btnadd.click", findAll.iterator().next().getKey());
//	}
	
}
