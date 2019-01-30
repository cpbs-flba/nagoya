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

package com.nagoya.dao.base;

import com.nagoya.model.dbo.SimpleDBO;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.exception.ResourceOutOfDateException;

public interface BasicDAO<T> {

    /**
     * Inserts the corresponding object into the DB.
     * 
     * @param dbo
     * @param createTransaction
     * @return
     */
    public SimpleDBO insert(SimpleDBO dbo, boolean createTransaction);

    /**
     * Searches for the respective object. If it is not found, null is returned. If more than one result is found, then an exception is thrown.
     * 
     * @param id
     * @param clazz
     * @return
     * @throws NonUniqueResultException
     */
    public SimpleDBO find(long id, final Class<T> type)
        throws NonUniqueResultException;

    /**
     * 
     * @param dBObject
     * @param clazz
     * @param createTransaction
     * @return
     */
    public SimpleDBO update(SimpleDBO dbo, boolean createTransaction)
        throws InvalidObjectException, ResourceOutOfDateException;

    /**
     * Deletes the object with the specified ID.
     * 
     * @param dbo
     * @param clazz
     * @param createTransaction
     * @return
     * @throws InvalidObjectException
     * @throws NonUniqueResultException
     */
    public boolean delete(SimpleDBO dbo, boolean createTransaction)
        throws InvalidObjectException;

}
