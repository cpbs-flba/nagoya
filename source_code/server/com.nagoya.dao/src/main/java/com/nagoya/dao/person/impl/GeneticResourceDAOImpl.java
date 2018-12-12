package com.nagoya.dao.person.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.person.GeneticResourceDAO;

public class GeneticResourceDAOImpl extends BasicDAOImpl<com.nagoya.model.dbo.resource.GeneticResource> implements GeneticResourceDAO {

	private static final Logger LOGGER = LogManager.getLogger(GeneticResourceDAOImpl.class);

	private Session session;

	public GeneticResourceDAOImpl(Session session) {
		super(session);
		this.session = session;
	}

	
}
