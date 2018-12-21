package com.nagoya.dao.person;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.dao.DAOTest;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.model.dbo.person.PersonKeys;
import com.nagoya.model.dbo.person.PersonNatural;

public class SearchPersonDAOTest extends DAOTest {

	private Session session = null;

	@BeforeEach
	public void init() {
		session = super.getSession();
		initializeEnvironment(session);
	}

	@Test
	@DisplayName("Test searchNaturaling for a person")
	public void searchNaturalTest1() throws Exception {

		// save the legal person
		PersonDAO dao = new PersonDAOImpl(session);
		dao.insert(getNaturalTestPerson("test@test.com", "max", "mustermann"), true);

		List<PersonNatural> searchNatural = dao.searchNatural("test@test.com", 10);
		Assert.assertEquals(1, searchNatural.size());
	}

	@Test
	@DisplayName("Test searchNaturaling for a person")
	public void searchNaturalTest2() throws Exception {

		// save the legal person
		PersonDAO dao = new PersonDAOImpl(session);

		PersonNatural n1 = getNaturalTestPerson("test1@test.com", "max", "mustermann");
		PersonKeys k1 = new PersonKeys();
		k1.setPublicKey("pk1");
		n1.getKeys().add(k1);
		dao.insert(n1, true);

		PersonNatural n2 = getNaturalTestPerson("test2@test.com", "maxi", "mustermann");
		PersonKeys k2 = new PersonKeys();
		k2.setPublicKey("pk2");
		n2.getKeys().add(k2);
		dao.insert(n2, true);

		List<PersonNatural> searchNatural = dao.searchNatural("test", 10);
		Assert.assertEquals(2, searchNatural.size());

		searchNatural = dao.searchNatural("test maxi", 10);
		Assert.assertEquals(1, searchNatural.size());

		searchNatural = dao.searchNatural("test max", 10);
		Assert.assertEquals(2, searchNatural.size());
		
		searchNatural = dao.searchNatural("pk1", 10);
		Assert.assertEquals(1, searchNatural.size());
	}

	private PersonNatural getNaturalTestPerson(String emailpw, String firstname, String lastname) {
		PersonNatural p = new PersonNatural();
		p.setBirthdate(new Date());
		p.setEmail(emailpw);
		p.setPassword(emailpw);
		p.setFirstname(firstname);
		p.setLastname(lastname);
		return p;
	}

}
