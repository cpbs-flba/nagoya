package com.nagoya.dao.base;

import com.nagoya.model.dbo.DBO;
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
	public DBO insert(DBO dbo, boolean createTransaction);

	/**
	 * Searches for the respective object. If it is not found, null is returned. If
	 * more than one result is found, then an exception is thrown.
	 * 
	 * @param id
	 * @param clazz
	 * @return
	 * @throws NonUniqueResultException
	 */
	public DBO find(long id, final Class<T> type) throws NonUniqueResultException;

	/**
	 * 
	 * @param dBObject
	 * @param clazz
	 * @param createTransaction
	 * @return
	 */
	public DBO update(DBO dbo, boolean createTransaction)
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
	public boolean delete(DBO dbo, boolean createTransaction)
			throws InvalidObjectException;

}
