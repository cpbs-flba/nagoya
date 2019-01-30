/*******************************************************************************
 * Copyright (c) 2004 - 2019 CPB Software AG
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS".
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 *
 * This software is published under the Apache License, Version 2.0, January 2004, 
 * http://www.apache.org/licenses/
 *  
 * Author: Florin Bogdan Balint
 *******************************************************************************/

package com.nagoya.dao.contract.impl;

import java.util.ArrayList;
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
import com.nagoya.model.dbo.contract.ContractDBO;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.person.PersonDBO;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractDAOImpl extends BasicDAOImpl<com.nagoya.model.dbo.contract.ContractDBO> implements ContractDAO {

    private Session session;

    public ContractDAOImpl(Session session) {
        super(session);
        this.session = session;
    }

    @Override
    public List<ContractDBO> search(PersonDBO person, Date periodFrom, Date periodUntil, Status status, int maxResults) {
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
        CriteriaQuery<ContractDBO> criteriaQuery = criteriaBuilder.createQuery(ContractDBO.class);

        Root<ContractDBO> root = criteriaQuery.from(ContractDBO.class);

        // owner condition
        Predicate c1 = criteriaBuilder.equal(root.get("sender"), person);
        Predicate c2 = criteriaBuilder.equal(root.get("receiver"), person);
        Predicate mandatoryCondition = criteriaBuilder.or(c1, c2);

        List<Predicate> additionalPredicates = new ArrayList<>();
        if (periodFrom != null) {
            Predicate greaterThanOrEqualTo = criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), periodFrom);
            additionalPredicates.add(greaterThanOrEqualTo);
        }
        if (periodUntil != null) {
            Predicate greaterThanOrEqualTo = criteriaBuilder.lessThanOrEqualTo(root.get("creationDate"), periodUntil);
            additionalPredicates.add(greaterThanOrEqualTo);
        }
        if (status != null) {
            Predicate statusQuery = criteriaBuilder.equal(root.get("status"), status);
            additionalPredicates.add(statusQuery);
        }

        Predicate finalCondition = mandatoryCondition;
        if (additionalPredicates.size() > 0) {
            additionalPredicates.add(mandatoryCondition);
            finalCondition = criteriaBuilder.and(additionalPredicates.toArray(new Predicate[] {}));
        }

        criteriaQuery.select(root).where(finalCondition);
        TypedQuery<ContractDBO> createdQuery = entityManager.createQuery(criteriaQuery);
        createdQuery.setMaxResults(maxResults);
        List<ContractDBO> resultList = createdQuery.getResultList();
        return resultList;
    }

}
