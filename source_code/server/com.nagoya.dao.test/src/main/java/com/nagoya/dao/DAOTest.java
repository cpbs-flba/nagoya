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

package com.nagoya.dao;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.common.blockchain.api.impl.BlockchainDriverImpl;
import com.nagoya.common.crypto.AESEncryptionProvider;
import com.nagoya.common.crypto.DefaultPasswordEncryptionProvider;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.dao.util.DefaultDBOFiller;
import com.nagoya.model.blockchain.Credentials;
import com.nagoya.model.dbo.person.AddressDBO;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.person.PersonKeysDBO;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.person.PersonNaturalDBO;
import com.nagoya.model.dbo.person.PersonType;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.ResourceFileDBO;
import com.nagoya.model.dbo.resource.TaxonomyDBO;
import com.nagoya.model.dbo.resource.VisibilityType;

public class DAOTest {

    private static final Logger LOGGER = LogManager.getLogger(DAOTest.class);

    public void initializeEnvironment(Session session) {
        LOGGER.info("Clearing environment.");

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.createNativeQuery("DELETE FROM taddress").executeUpdate();
            session.createNativeQuery("DELETE FROM taddress_aud").executeUpdate();

            session.createNativeQuery("DELETE FROM tperson").executeUpdate();
            session.createNativeQuery("DELETE FROM tperson_aud").executeUpdate();

            session.createNativeQuery("DELETE FROM tperson_natural").executeUpdate();
            session.createNativeQuery("DELETE FROM tperson_natural_aud").executeUpdate();

            session.createNativeQuery("DELETE FROM tperson_legal").executeUpdate();
            session.createNativeQuery("DELETE FROM tperson_legal_aud").executeUpdate();

            session.createNativeQuery("DELETE FROM tgenetic_resource").executeUpdate();
            session.createNativeQuery("DELETE FROM tgenetic_resource_aud").executeUpdate();

            session.createNativeQuery("DELETE FROM tgenetic_resource_taxonomy").executeUpdate();

            session.createNativeQuery("DELETE FROM tgenetic_resource_file").executeUpdate();

            session.createNativeQuery("DELETE FROM tperson_group").executeUpdate();
            session.createNativeQuery("DELETE FROM tperson_group_aud").executeUpdate();

            session.createNativeQuery("DELETE FROM tcontract").executeUpdate();
            session.createNativeQuery("DELETE FROM tcontract_aud").executeUpdate();

            session.createNativeQuery("DELETE FROM tcontract_resource").executeUpdate();
            session.createNativeQuery("DELETE FROM tcontract_resource_aud").executeUpdate();

            session.createNativeQuery("DELETE FROM tcontract_file").executeUpdate();

            session.createNativeQuery("DELETE FROM tonline_user").executeUpdate();

            session.createNativeQuery("DELETE FROM tuser_request").executeUpdate();

            session.createNativeQuery("DELETE FROM tperson_keys").executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOGGER.error(e);
        }

    }

    public PersonLegalDBO insertTestPersonLegal(Session session, String email) {
        AddressDBO address = new AddressDBO();
        address.setStreet("Max Street");
        address.setNumber("1");
        address.setZip("1010");
        address.setCountry("Austria");
        address.setCity("Vienna");

        PersonLegalDBO legalPerson = new PersonLegalDBO();
        legalPerson.setEmailConfirmed(true);
        legalPerson.setEmail(email);
        legalPerson.setPassword(DefaultPasswordEncryptionProvider.encryptPassword(email));
        legalPerson.setAddress(address);
        legalPerson.setStorePrivateKey(true);

        BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
        Credentials credentials = blockchainDriver.createCredentials();

        PersonKeysDBO pk = new PersonKeysDBO();
        pk.setPublicKey(credentials.getPublicKey());

        String privateKeyEncrypted = AESEncryptionProvider.encrypt(credentials.getPrivateKey(), email.toLowerCase());
        pk.setPrivateKey(privateKeyEncrypted);

        pk.setActive(true);
        LOGGER.info("Private key for: " + email + ", is: " + credentials.getPrivateKey());
        legalPerson.getKeys().add(pk);

        Random rn = new Random();
        int answer = rn.nextInt(100) + 1;
        legalPerson.setName("Legal Corp. No." + answer);

        int answer2 = rn.nextInt(100) + 1;
        legalPerson.setCommercialRegisterNumber("FN" + answer2);

        int answer3 = rn.nextInt(100) + 1;
        legalPerson.setTaxNumber("TN" + answer3);
        legalPerson.setPersonType(PersonType.LEGAL);

        PersonDAO personDAO = new PersonDAOImpl(session);
        return (PersonLegalDBO) personDAO.insert(legalPerson, true);
    }

    public GeneticResourceDBO insertTestGeneticResource(Session session, PersonDBO owner, String idName, VisibilityType visibilityType,
        TaxonomyDBO t6) {
        GeneticResourceDBO resource1 = new GeneticResourceDBO();
        resource1.setIdentifier(idName);
        resource1.setDescription(idName);
        resource1.setOwner(owner);
        resource1.setVisibilityType(visibilityType);
        resource1.setSource("Brasil");
        resource1.setOrigin("Argentina");
        resource1.setHashSequence("Hash123456");

        ResourceFileDBO rf = new ResourceFileDBO();
        rf.setContent("test".getBytes());
        rf.setName("sometext");
        rf.setType("txt");
        resource1.getFiles().add(rf);

        resource1.setTaxonomy(t6);

        PersonDAO personDAO = new PersonDAOImpl(session);
        return (GeneticResourceDBO) personDAO.insert(resource1, true);
    }

    public TaxonomyDBO getTaxonomyParent() {
        TaxonomyDBO t1 = new TaxonomyDBO();
        t1.setName("Plantae");

        TaxonomyDBO t2 = new TaxonomyDBO();
        t2.setName("Angiosperms");
        t2.setParent(t1);

        return t2;
    }

    public TaxonomyDBO getTaxonomyChild1(TaxonomyDBO parent) {
        TaxonomyDBO t3 = new TaxonomyDBO();
        t3.setName("Asteraceae 1");
        t3.setParent(parent);

        TaxonomyDBO t4 = new TaxonomyDBO();
        t4.setName("Asteroideae 1");
        t4.setParent(t3);

        return t4;
    }

    public TaxonomyDBO getTaxonomyChild2(TaxonomyDBO parent) {
        TaxonomyDBO t3 = new TaxonomyDBO();
        t3.setName("Asteraceae 2");
        t3.setParent(parent);

        TaxonomyDBO t4 = new TaxonomyDBO();
        t4.setName("Asteroideae 2");
        t4.setParent(t3);

        return t4;
    }

    public PersonNaturalDBO insertTestPersonNatural(Session session, String email, String firstname, String lastname) {
        AddressDBO address = new AddressDBO();
        address.setStreet("Max Street");
        address.setNumber("1");
        address.setZip("1010");
        address.setCountry("Austria");
        address.setCity("Vienna");

        PersonNaturalDBO person = new PersonNaturalDBO();
        person.setFirstname(firstname);
        person.setLastname(lastname);
        person.setBirthdate(new java.util.Date());
        person.setEmailConfirmed(true);
        person.setEmail(email.toLowerCase());
        person.setStorePrivateKey(true);
        person.setPassword(DefaultPasswordEncryptionProvider.encryptPassword(email.toLowerCase()));
        person.setAddress(address);

        BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
        Credentials credentials = blockchainDriver.createCredentials();

        PersonKeysDBO pk = new PersonKeysDBO();
        pk.setActive(true);
        DefaultDBOFiller.fillDefaultDataObjectValues(pk);
        pk.setPublicKey(credentials.getPublicKey());

        LOGGER.info("Private key for: " + email + ", is: " + credentials.getPrivateKey());

        String privateKeyEncrypted = AESEncryptionProvider.encrypt(credentials.getPrivateKey(), email.toLowerCase());
        pk.setPrivateKey(privateKeyEncrypted);
        person.getKeys().add(pk);

        PersonDAO personDAO = new PersonDAOImpl(session);
        personDAO.insert(person, true);
        return person;
    }

    public Session getSession() {
        return TestConnectionProvider.getInstance().getSession();
    }

}
