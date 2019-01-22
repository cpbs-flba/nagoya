
package com.nagoya.dao.person.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.person.PersonKeysDBO;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.person.PersonNaturalDBO;
import com.nagoya.model.dbo.user.OnlineUserDBO;
import com.nagoya.model.dbo.user.UserRequestDBO;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.InvalidObjectException;

public class PersonDAOImpl extends BasicDAOImpl<PersonDBO> implements PersonDAO {

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
    public PersonDBO register(PersonDBO person)
        throws ConflictException {
        PersonDBO existent = findPersonForEmail(person.getEmail());
        if (existent != null) {
            throw new ConflictException("E-mail already registered.");
        }
        PersonDBO inserted = (PersonDBO) this.insert(person, true);
        return inserted;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.dao.person.PersonDAO#findPersonForEmail(java.lang.String)
     */
    @Override
    public PersonDBO findPersonForEmail(String email)
        throws ConflictException {
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

        // Create criteriaQuery
        CriteriaQuery<PersonDBO> criteriaQuery = criteriaBuilder.createQuery(PersonDBO.class);

        Root<PersonDBO> root = criteriaQuery.from(PersonDBO.class);
        Predicate condition = criteriaBuilder.equal(root.get("email"), email.toLowerCase());
        criteriaQuery.select(root).where(condition);

        TypedQuery<PersonDBO> createdQuery = entityManager.createQuery(criteriaQuery);
        try {
            PersonDBO singleResult = createdQuery.getSingleResult();
            return singleResult;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new ConflictException("Too many results found for e-mail: " + email);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.dao.person.PersonDAO#removeOldSessions(com.nagoya.model.dbo.person .Person)
     */
    @Override
    public void removeOldSessions(PersonDBO person) {
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
     * 
     * @see com.nagoya.dao.person.PersonDAO#getOnlineUser(java.lang.String)
     */
    @Override
    public OnlineUserDBO getOnlineUser(String sessionToken)
        throws ConflictException {
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

        // Create criteriaQuery
        CriteriaQuery<OnlineUserDBO> criteriaQuery = criteriaBuilder.createQuery(OnlineUserDBO.class);

        Root<OnlineUserDBO> root = criteriaQuery.from(OnlineUserDBO.class);
        Predicate condition = criteriaBuilder.equal(root.get("sessionToken"), sessionToken);
        criteriaQuery.select(root).where(condition);

        TypedQuery<OnlineUserDBO> createdQuery = entityManager.createQuery(criteriaQuery);
        try {
            OnlineUserDBO singleResult = createdQuery.getSingleResult();
            return singleResult;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new ConflictException("Too many results found for sessionToken: " + sessionToken);
        }
    }

    @Override
    public UserRequestDBO findUserRequest(String token)
        throws ConflictException {
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

        // Create criteriaQuery
        CriteriaQuery<UserRequestDBO> criteriaQuery = criteriaBuilder.createQuery(UserRequestDBO.class);

        Root<UserRequestDBO> root = criteriaQuery.from(UserRequestDBO.class);
        Predicate condition = criteriaBuilder.equal(root.get("token"), token);
        criteriaQuery.select(root).where(condition);

        TypedQuery<UserRequestDBO> createdQuery = entityManager.createQuery(criteriaQuery);
        try {
            UserRequestDBO singleResult = createdQuery.getSingleResult();
            return singleResult;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new ConflictException("Too many results found for token: " + token);
        }
    }

    @Override
    public void delete(PersonDBO person)
        throws InvalidObjectException {
        // first delete the person data normally
        Long id = person.getId();
        super.delete(person, true);

        // now clean up all history tables
        String query1 = "DELETE FROM tperson_aud WHERE id=" + id.toString();
        String query2 = "DELETE FROM tperson_legal_aud WHERE person_id=" + id.toString();
        String query3 = "DELETE FROM tperson_natural_aud WHERE person_id=" + id.toString();
        String query4 = "DELETE FROM tuser_request WHERE person_id=" + id.toString();
        String query5 = "DELETE FROM tonline_user WHERE person_id=" + id.toString();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createNativeQuery(query1).executeUpdate();
            session.createNativeQuery(query2).executeUpdate();
            session.createNativeQuery(query3).executeUpdate();
            session.createNativeQuery(query4).executeUpdate();
            session.createNativeQuery(query5).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<PersonNaturalDBO> searchNatural(String filter, int maxResults) {
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

        // Create criteriaQuery
        CriteriaQuery<PersonNaturalDBO> criteriaQuery = criteriaBuilder.createQuery(PersonNaturalDBO.class);

        Root<PersonNaturalDBO> root = criteriaQuery.from(PersonNaturalDBO.class);

        List<Predicate> blockPredicates = new ArrayList<Predicate>();

        if (StringUtil.isNotNullOrBlank(filter)) {
            Pattern pattern = Pattern.compile("\\w+");
            Matcher matcher = pattern.matcher(filter);
            while (matcher.find()) {
                String singleWord = matcher.group();

                Predicate p1 = criteriaBuilder.like(root.get("firstname"), "%" + singleWord + "%");
                Predicate p2 = criteriaBuilder.like(root.get("lastname"), "%" + singleWord + "%");
                Predicate p3 = criteriaBuilder.like(root.get("email"), "%" + singleWord + "%");

                SetJoin<PersonNaturalDBO, PersonKeysDBO> children = root.joinSet("keys", JoinType.LEFT);
                Predicate p4 = criteriaBuilder.equal(children.get("publicKey"), singleWord);

                Predicate blockPredicate = criteriaBuilder.or(p1, p2, p3, p4);
                blockPredicates.add(blockPredicate);
            }
        }
        Predicate finalWordsPredicate = criteriaBuilder.and(blockPredicates.toArray(new Predicate[] {}));
        criteriaQuery.select(root).where(finalWordsPredicate);

        // set a default value
        if (maxResults <= 0) {
            maxResults = 20;
        }
        TypedQuery<PersonNaturalDBO> createdQuery = entityManager.createQuery(criteriaQuery);
        List<PersonNaturalDBO> results = createdQuery.getResultList();
        return results;

    }

    @Override
    public List<PersonLegalDBO> searchLegal(String filter, int maxResults) {
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

        // Create criteriaQuery
        CriteriaQuery<PersonLegalDBO> criteriaQuery = criteriaBuilder.createQuery(PersonLegalDBO.class);

        Root<PersonLegalDBO> root = criteriaQuery.from(PersonLegalDBO.class);

        List<Predicate> blockPredicates = new ArrayList<Predicate>();

        if (StringUtil.isNotNullOrBlank(filter)) {
            Pattern pattern = Pattern.compile("\\w+");
            Matcher matcher = pattern.matcher(filter);
            while (matcher.find()) {
                String singleWord = matcher.group();

                Predicate p1 = criteriaBuilder.like(root.get("name"), "%" + singleWord + "%");
                Predicate p2 = criteriaBuilder.like(root.get("commercialRegisterNumber"), "%" + singleWord + "%");
                Predicate p3 = criteriaBuilder.like(root.get("taxNumber"), "%" + singleWord + "%");

                SetJoin<PersonLegalDBO, PersonKeysDBO> children = root.joinSet("keys", JoinType.LEFT);
                Predicate p4 = criteriaBuilder.equal(children.get("publicKey"), singleWord);

                Predicate blockPredicate = criteriaBuilder.or(p1, p2, p3, p4);
                blockPredicates.add(blockPredicate);
            }
        }
        Predicate finalWordsPredicate = criteriaBuilder.and(blockPredicates.toArray(new Predicate[] {}));
        criteriaQuery.select(root).where(finalWordsPredicate);

        // set a default value
        if (maxResults <= 0) {
            maxResults = 20;
        }
        TypedQuery<PersonLegalDBO> createdQuery = entityManager.createQuery(criteriaQuery);
        List<PersonLegalDBO> results = createdQuery.getResultList();
        return results;

    }

}
