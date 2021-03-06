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

package com.nagoya.dao.person;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nagoya.dao.DAOTest;
import com.nagoya.dao.base.BasicDAO;
import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.model.dbo.person.AddressDBO;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.person.PersonType;
import com.nagoya.model.dbo.person.group.PersonGroupDBO;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.ResourceFileDBO;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.ResourceOutOfDateException;

public class PersonDAOTest extends DAOTest {

    private Session session = null;

    @BeforeEach
    public void init() {
        session = super.getSession();
        initializeEnvironment(session);
    }

    @Test
    // @DisplayName("Test person: legal person insert, update and delete")
    public void insertAndDeleteLegalPerson()
        throws InvalidObjectException, ResourceOutOfDateException {
        AddressDBO address = new AddressDBO();
        address.setStreet("s");
        address.setNumber("132");
        address.setZip("132");
        address.setCountry("Bla");
        address.setCity("A");

        PersonLegalDBO legalPerson = new PersonLegalDBO();
        legalPerson.setEmail("legal@legal.com");
        legalPerson.setPassword("secret");
        legalPerson.setAddress(address);
        legalPerson.setName("Legal Corp.");
        legalPerson.setCommercialRegisterNumber("c123");
        legalPerson.setTaxNumber("tax123");
        legalPerson.setPersonType(PersonType.LEGAL);

        // save the legal person
        BasicDAO<PersonDBO> personDAO = new BasicDAOImpl<PersonDBO>(session);
        personDAO.insert(legalPerson, true);
        Assert.assertNotNull(address.getId());
        Assert.assertNotNull(legalPerson.getId());

        PersonGroupDBO pg = new PersonGroupDBO();
        pg.setName("testgroup");
        pg.getPersons().add(legalPerson);
        personDAO.insert(pg, true);

        GeneticResourceDBO resource = new GeneticResourceDBO();
        resource.setIdentifier("i123");
        resource.setDescription("sonneblume");
        resource.setOwner(legalPerson);
        resource.setVisibilityType(VisibilityType.PUBLIC);
        resource.setSource("Brasil");
        ResourceFileDBO rf = new ResourceFileDBO();
        rf.setContent("test".getBytes());
        rf.setName("sometext");
        rf.setType("txt");
        resource.getFiles().add(rf);
        personDAO.insert(resource, true);

        // update the legal person
        legalPerson.setName("Legal Corp. 2 ");
        personDAO.update(legalPerson, true);
    }

}
