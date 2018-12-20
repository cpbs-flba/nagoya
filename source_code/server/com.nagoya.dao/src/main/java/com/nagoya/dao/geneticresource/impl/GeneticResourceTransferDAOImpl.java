package com.nagoya.dao.geneticresource.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.geneticresource.GeneticResourceTransferDAO;
import com.nagoya.model.dbo.resource.GeneticResourceTransfer;
import com.nagoya.model.exception.NonUniqueResultException;

public class GeneticResourceTransferDAOImpl extends BasicDAOImpl<com.nagoya.model.dbo.resource.GeneticResourceTransfer>
		implements GeneticResourceTransferDAO {

	private static final Logger LOGGER = LogManager.getLogger(GeneticResourceTransferDAOImpl.class);

	public GeneticResourceTransferDAOImpl(Session session) {
		super(session);
		LOGGER.debug("Initializing DAO");
	}

	@Override
	public GeneticResourceTransfer find(long id) throws NonUniqueResultException {
		return (GeneticResourceTransfer) find(id, GeneticResourceTransfer.class);
	}

}
