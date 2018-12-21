package com.nagoya.dao.geneticresource;

import com.nagoya.dao.base.BasicDAO;
import com.nagoya.model.dbo.resource.GeneticResourceTransfer;
import com.nagoya.model.exception.NonUniqueResultException;

public interface GeneticResourceTransferDAO extends BasicDAO<com.nagoya.model.dbo.resource.GeneticResourceTransfer> {

	public GeneticResourceTransfer find(long id) throws NonUniqueResultException;

}
