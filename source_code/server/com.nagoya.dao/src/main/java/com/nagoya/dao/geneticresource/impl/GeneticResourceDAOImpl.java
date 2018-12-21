package com.nagoya.dao.geneticresource.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.model.dbo.person.Person;
import com.nagoya.model.dbo.resource.GeneticResource;
import com.nagoya.model.dbo.resource.Taxonomy;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

public class GeneticResourceDAOImpl extends BasicDAOImpl<com.nagoya.model.dbo.resource.GeneticResource>
		implements GeneticResourceDAO {

	private static final Logger LOGGER = LogManager.getLogger(GeneticResourceDAOImpl.class);

	private Session session;

	public GeneticResourceDAOImpl(Session session) {
		super(session);
		this.session = session;
	}

	@Override
	public List<GeneticResource> search(GeneticResourceFilter filter, Person owner, int maxResults) {
		LOGGER.debug("Searching for genetic resource.");

		EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		// Create criteriaQuery
		CriteriaQuery<GeneticResource> criteriaQuery = criteriaBuilder.createQuery(GeneticResource.class);
		Root<GeneticResource> root = criteriaQuery.from(GeneticResource.class);
		
		// we have multiple filters that can be applied
		List<Predicate> visibilityPredicates = new ArrayList<Predicate>();

		// set the visibility
		Predicate publicResources = criteriaBuilder.equal(root.get("visibilityType"), VisibilityType.PUBLIC);
		visibilityPredicates.add(publicResources);
		if (owner != null) {
			Predicate ownerRessource = criteriaBuilder.equal(root.get("owner"), owner);
			visibilityPredicates.add(ownerRessource);
		}
		
		// TODO: ADD group visibility query
		
		// now apply the visibility as an OR
		Predicate visibilityCondition = criteriaBuilder.or(visibilityPredicates.toArray(new Predicate[] {}));

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(visibilityCondition);
		
		if (filter == null) {
			filter = new GeneticResourceFilter();
		}

		String identifier = filter.getIdentifier();
		if (StringUtil.isNotNullOrBlank(identifier)) {
			Predicate p = criteriaBuilder.like(root.get("identifier"), "%" + identifier + "%");
			predicates.add(p);
		}

		String hashSequence = filter.getHashSequence();
		if (StringUtil.isNotNullOrBlank(hashSequence)) {
			Predicate p = criteriaBuilder.like(root.get("hashSequence"), "%" + hashSequence + "%");
			predicates.add(p);
		}

		String description = filter.getDescription();
		if (StringUtil.isNotNullOrBlank(description)) {
			Predicate p = criteriaBuilder.like(root.get("description"), "%" + description + "%");
			predicates.add(p);
		}

		Predicate condition = criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
		criteriaQuery.select(root).where(condition);

		// set a default value
		if (maxResults <= 0) {
			maxResults = 20;
		}

		TypedQuery<GeneticResource> createdQuery = entityManager.createQuery(criteriaQuery);
		createdQuery.setMaxResults(maxResults);
		List<GeneticResource> resultList = createdQuery.getResultList();
		return resultList;
	}

	@Override
	public List<Taxonomy> getTaxonomyRootLevel() {
		EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		// Create criteriaQuery
		CriteriaQuery<Taxonomy> criteriaQuery = criteriaBuilder.createQuery(Taxonomy.class);

		Root<Taxonomy> root = criteriaQuery.from(Taxonomy.class);
		Predicate condition = criteriaBuilder.isNull(root.get("parent"));
		criteriaQuery.select(root).where(condition);

		TypedQuery<Taxonomy> createdQuery = entityManager.createQuery(criteriaQuery);
		List<Taxonomy> resultList = createdQuery.getResultList();
		return resultList;
	}

	@Override
	public List<Taxonomy> getTaxonomyChildren(long parentId) {
		EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		Taxonomy parentTaxonomy = findTaxonomy(parentId);
		if (parentTaxonomy == null) {
			return new ArrayList<>();
		}
		
		// Create criteriaQuery
		CriteriaQuery<Taxonomy> criteriaQuery = criteriaBuilder.createQuery(Taxonomy.class);

		Root<Taxonomy> root = criteriaQuery.from(Taxonomy.class);
		Predicate condition = criteriaBuilder.equal(root.get("parent"), parentTaxonomy);
		criteriaQuery.select(root).where(condition);

		TypedQuery<Taxonomy> createdQuery = entityManager.createQuery(criteriaQuery);
		List<Taxonomy> resultList = createdQuery.getResultList();
		return resultList;
	}
	
	
	private Taxonomy findTaxonomy(long id) throws NonUniqueResultException {
		EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		// Create criteriaQuery
		CriteriaQuery<Taxonomy> criteriaQuery = criteriaBuilder.createQuery(Taxonomy.class);

		Root<Taxonomy> root = criteriaQuery.from(Taxonomy.class);
		Predicate condition = criteriaBuilder.equal(root.get("id"), id);
		criteriaQuery.select(root).where(condition);

		TypedQuery<?> createdQuery = entityManager.createQuery(criteriaQuery);
		try {
			Taxonomy singleResult = (Taxonomy) createdQuery.getSingleResult();
			return singleResult;
		} catch (NonUniqueResultException e) {
			throw new NonUniqueResultException(e.getMessage());
		} catch (NoResultException e) {
			return null;
		}
	}

}
