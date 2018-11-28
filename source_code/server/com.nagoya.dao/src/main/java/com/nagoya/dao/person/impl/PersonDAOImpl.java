package com.nagoya.dao.person.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.model.dbo.person.Person;
import com.nagoya.model.dbo.user.OnlineUser;
import com.nagoya.model.dbo.user.UserRequest;
import com.nagoya.model.exception.ConflictException;

public class PersonDAOImpl extends BasicDAOImpl<Person> implements PersonDAO {

	private Session session;

	public PersonDAOImpl(Session session) {
		super(session);
		this.session = session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nagoya.dao.person.PersonDAO#register(java.lang.String)
	 */
	@Override
	public Person register(Person person) throws ConflictException {
		Person existent = findPersonForEmail(person.getEmail());
		if (existent != null) {
			throw new ConflictException("E-mail already registered.");
		}
		Person inserted = (Person) this.insert(person, true);
		return inserted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nagoya.dao.person.PersonDAO#findPersonForEmail(java.lang.String)
	 */
	@Override
	public Person findPersonForEmail(String email) throws ConflictException {
		EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		// Create criteriaQuery
		CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);

		Root<Person> root = criteriaQuery.from(Person.class);
		Predicate condition = criteriaBuilder.equal(root.get("email"), email);
		criteriaQuery.select(root).where(condition);

		TypedQuery<Person> createdQuery = entityManager.createQuery(criteriaQuery);
		try {
			Person singleResult = createdQuery.getSingleResult();
			return singleResult;
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new ConflictException("Too many results found for e-mail: " + email);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.nagoya.dao.person.PersonDAO#removeOldSessions(com.nagoya.model.dbo.person.Person)
	 */
	@Override
	public void removeOldSessions(Person person) {
		String query = "DELETE FROM tonline_user WHERE person_id=" + person.getId();

		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.createNativeQuery(query).executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.nagoya.dao.person.PersonDAO#getOnlineUser(java.lang.String)
	 */
	@Override
	public OnlineUser getOnlineUser(String sessionToken) throws ConflictException {
		EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		// Create criteriaQuery
		CriteriaQuery<OnlineUser> criteriaQuery = criteriaBuilder.createQuery(OnlineUser.class);

		Root<OnlineUser> root = criteriaQuery.from(OnlineUser.class);
		Predicate condition = criteriaBuilder.equal(root.get("session_token"), sessionToken);
		criteriaQuery.select(root).where(condition);

		TypedQuery<OnlineUser> createdQuery = entityManager.createQuery(criteriaQuery);
		try {
			OnlineUser singleResult = createdQuery.getSingleResult();
			return singleResult;
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new ConflictException("Too many results found for sessionToken: " + sessionToken);
		}
	}

	@Override
	public UserRequest findUserRequest(String token) throws ConflictException {
		EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		// Create criteriaQuery
		CriteriaQuery<UserRequest> criteriaQuery = criteriaBuilder.createQuery(UserRequest.class);

		Root<UserRequest> root = criteriaQuery.from(UserRequest.class);
		Predicate condition = criteriaBuilder.equal(root.get("token"), token);
		criteriaQuery.select(root).where(condition);

		TypedQuery<UserRequest> createdQuery = entityManager.createQuery(criteriaQuery);
		try {
			UserRequest singleResult = createdQuery.getSingleResult();
			return singleResult;
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new ConflictException("Too many results found for token: " + token);
		}
	}

}
