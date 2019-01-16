/**
 * (C) Copyright 2004 - 2019 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : Florin Bogdan Balint
 */

package com.nagoya.dao.contract.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.model.dbo.contract.Contract;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.person.Person;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractDAOImpl extends BasicDAOImpl<com.nagoya.model.dbo.contract.Contract> implements ContractDAO {

    private Session session;

    public ContractDAOImpl(Session session) {
        super(session);
        this.session = session;
    }

    @Override
    public List<Contract> search(Person person, Date periodFrom, Date periodUntil, Status status, int maxResults) {
        // fail-safe
        if (person == null) {
            return null;
        }
        // set a default value
        if (maxResults <= 0) {
            maxResults = 100;
        }

        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

        // Create criteriaQuery
        CriteriaQuery<Contract> criteriaQuery = criteriaBuilder.createQuery(Contract.class);

        Root<Contract> root = criteriaQuery.from(Contract.class);

        // owner condition
        Predicate c1 = criteriaBuilder.equal(root.get("sender"), person);
        Predicate c2 = criteriaBuilder.equal(root.get("receiver"), person);
        Predicate involvedPersonCondition = criteriaBuilder.or(c1, c2);

        criteriaQuery.select(root).where(involvedPersonCondition);
        TypedQuery<Contract> createdQuery = entityManager.createQuery(criteriaQuery);
        createdQuery.setMaxResults(maxResults);
        List<Contract> resultList = createdQuery.getResultList();
        return resultList;
    }

}
