
package com.nagoya.middleware.test.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nagoya.dao.db.ConnectionProvider;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.model.dbo.person.Person;
import com.nagoya.model.dbo.resource.GeneticResource;
import com.nagoya.model.dbo.resource.ResourceFile;
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

            session.createNativeQuery("DELETE FROM tuser_request").executeUpdate();

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

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOGGER.error(e);
        }

    }

    public GeneticResource insertTestGeneticResource(Session session, Person owner, String idName) {
        GeneticResource resource1 = new GeneticResource();
        resource1.setIdentifier(idName);
        resource1.setDescription(idName);
        resource1.setOwner(owner);
        resource1.setVisibilityType(VisibilityType.PRIVATE);
        resource1.setSource("Brasil");
        ResourceFile rf = new ResourceFile();
        rf.setContent("test".getBytes());
        rf.setName("sometext");
        rf.setType("txt");
        resource1.getFiles().add(rf);

        PersonDAO personDAO = new PersonDAOImpl(session);
        return (GeneticResource) personDAO.insert(resource1, true);
    }

    public Session getSession() {
        return ConnectionProvider.getInstance().getSession();
    }

}
