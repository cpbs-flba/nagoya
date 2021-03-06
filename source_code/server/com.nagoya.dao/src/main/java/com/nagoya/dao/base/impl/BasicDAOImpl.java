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

package com.nagoya.dao.base.impl;

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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nagoya.dao.base.BasicDAO;
import com.nagoya.dao.util.DefaultDBOFiller;
import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.SimpleDBO;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.ResourceOutOfDateException;

/**
 * @author flba
 *
 */
public class BasicDAOImpl<T> implements BasicDAO<T> {

    private static final Logger LOGGER = LogManager.getLogger(BasicDAOImpl.class);

    private Session             session;

    public BasicDAOImpl(Session session) {
        this.session = session;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.dao.base.BasicDAO#insert(com.nagoya.model.dbo.DBO, boolean)
     */
    @Override
    public SimpleDBO insert(SimpleDBO dbo, boolean createTransaction) {
        LOGGER.trace("Inserting object into DB");
        DefaultDBOFiller.fillDefaultDataObjectValues(dbo);

        // if no transaction is needed, just persist the object and return
        if (createTransaction == false) {
            DBO savedObject = (DBO) session.save(dbo);
            return savedObject;
        }

        // otherwise create the transaction
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Long generatedId = (Long) session.save(dbo);
            session.flush();
            transaction.commit();
            dbo.setId(generatedId);
            return dbo;
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
     * @see com.nagoya.dao.base.BasicDAO#find(long, java.lang.Class)
     */
    @Override
    public SimpleDBO find(long id, final Class<T> type)
        throws NonUniqueResultException {
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

        // Create criteriaQuery
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);

        Root<T> root = criteriaQuery.from(type);
        Predicate condition = criteriaBuilder.equal(root.get("id"), id);
        criteriaQuery.select(root).where(condition);

        TypedQuery<?> createdQuery = entityManager.createQuery(criteriaQuery);
        try {
            SimpleDBO singleResult = (SimpleDBO) createdQuery.getSingleResult();
            return singleResult;
        } catch (NonUniqueResultException e) {
            throw new NonUniqueResultException(e.getMessage());
        } catch (NoResultException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.dao.base.BasicDAO#update(com.nagoya.model.dbo.DBO, boolean)
     */
    @Override
    public SimpleDBO update(SimpleDBO dbo, boolean createTransaction)
        throws InvalidObjectException, ResourceOutOfDateException {
        if (dbo.getId() == null) {
            throw new InvalidObjectException("Cannot update object who's ID is NULL.");
        }
        DefaultDBOFiller.fillDefaultDataObjectValues(dbo);
        if (createTransaction == false) {
            session.update(dbo);
            return dbo;
        }
        // otherwise create the transaction
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            DBO merged = (DBO) session.merge(dbo);
            session.update(merged);
            session.flush();
            session.clear();
            transaction.commit();
        } catch (org.hibernate.HibernateException | javax.persistence.OptimisticLockException e) {
            throw new ResourceOutOfDateException(e.getMessage());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return dbo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.dao.base.BasicDAO#delete(com.nagoya.model.dbo.DBO, boolean)
     */
    @Override
    public boolean delete(SimpleDBO dbo, boolean createTransaction)
        throws InvalidObjectException {
        if (dbo.getId() == null) {
            throw new InvalidObjectException("Cannot delete object who's ID is NULL.");
        }

        if (createTransaction == false) {
            session.delete(dbo);
        }
        // otherwise create the transaction
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(dbo);
            session.flush();
            session.clear();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return true;
    }

}
