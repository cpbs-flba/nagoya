
package com.nagoya.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.model.dbo.person.AddressDBO;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.person.PersonType;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.ResourceFileDBO;
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
        legalPerson.setEmail(email);
        legalPerson.setPassword(email);
        legalPerson.setAddress(address);
        legalPerson.setName("Legal Corp.");
        legalPerson.setCommercialRegisterNumber("c123");
        legalPerson.setTaxNumber("tax123");
        legalPerson.setPersonType(PersonType.LEGAL);

        PersonDAO personDAO = new PersonDAOImpl(session);
        return (PersonLegalDBO) personDAO.insert(legalPerson, true);
    }

    public GeneticResourceDBO insertTestGeneticResource(Session session, PersonDBO owner, String idName) {
        GeneticResourceDBO resource1 = new GeneticResourceDBO();
        resource1.setIdentifier(idName);
        resource1.setDescription(idName);
        resource1.setOwner(owner);
        resource1.setVisibilityType(VisibilityType.PRIVATE);
        resource1.setSource("Brasil");
        ResourceFileDBO rf = new ResourceFileDBO();
        rf.setContent("test".getBytes());
        rf.setName("sometext");
        rf.setType("txt");
        resource1.getFiles().add(rf);

        PersonDAO personDAO = new PersonDAOImpl(session);
        return (GeneticResourceDBO) personDAO.insert(resource1, true);
    }

    public Session getSession() {
        return TestConnectionProvider.getInstance().getSession();
    }

}
