
package com.nagoya.middleware.test.it;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.common.crypto.DefaultPasswordEncryptionProvider;
import com.nagoya.dao.base.BasicDAO;
import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.middleware.test.base.RestBaseTest;
import com.nagoya.model.dbo.person.PersonLegal;
import com.nagoya.model.dbo.resource.GeneticResource;
import com.nagoya.model.dto.contract.Contract;
import com.nagoya.model.dto.contract.ContractResource;
import com.nagoya.model.to.person.Person;

/**
 * @author flba
 *
 */
public class ContractITTest extends RestBaseTest {

    private static final Logger LOGGER = LogManager.getLogger(ContractITTest.class);

    @Test
    @DisplayName("simple contract creation test")
    public void createContractTest()
        throws Exception {
        // set this property to TRUE and no e-mails will be sent
        System.setProperty("nagoya.test", "true");

        // insert some dummy data
        PersonLegal p1 = insertDummyLegalPerson();

        // insert the genetic resource
        GeneticResource insertedGeneticResource = insertTestGeneticResource(getSession(), p1, "bla123");

        // now login
        String targetUrl = serverURL + "/users/login";
        LOGGER.debug("Sending request POST: " + targetUrl);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(targetUrl);

        com.nagoya.model.to.person.PersonLegal personTO = new com.nagoya.model.to.person.PersonLegal();
        personTO.setEmail("test@test.com1");
        personTO.setPassword("test@test.com1");
        Entity<com.nagoya.model.to.person.PersonLegal> entity = Entity.entity(personTO, MediaType.APPLICATION_JSON);

        Response response = target.request(MediaType.APPLICATION_JSON).post(entity);
        String authHeader = response.getHeaderString("Authorization");
        int status = response.getStatus();
        response.close();
        Assert.assertEquals(200, status);

        // now create a contract
        Person receiver = new Person();
        receiver.setEmail("test@test.com2");

        Contract contractTO = new Contract();
        contractTO.setReceiver(receiver);

        ContractResource cr1 = new ContractResource();
        cr1.setAmount(100);
        com.nagoya.model.to.resource.GeneticResource geneticResource = new com.nagoya.model.to.resource.GeneticResource();
        geneticResource.setId(insertedGeneticResource.getId());
        cr1.setGeneticResource(geneticResource);
        cr1.setMeasuringUnit("unit");
        contractTO.getContractResources().add(cr1);

        // now lets put this contract
        targetUrl = serverURL + "/contracts";
        client = ClientBuilder.newClient();
        target = client.target(targetUrl);
        Entity<Contract> entity2 = Entity.entity(contractTO, MediaType.APPLICATION_JSON);
        response = target //
            .request(MediaType.APPLICATION_JSON)//
            .header("Language", "de") //
            .header("Authorization", authHeader) //
            .put(entity2);
        status = response.getStatus();
        response.close();

        Assert.assertEquals(204, status);
    }

    private com.nagoya.model.dbo.person.PersonLegal insertDummyLegalPerson() {
        // save the legal person
        com.nagoya.model.dbo.person.PersonLegal result = null;
        BasicDAO<com.nagoya.model.dbo.person.PersonLegal> personDAO = new BasicDAOImpl<com.nagoya.model.dbo.person.PersonLegal>(getSession());
        for (int i = 0; i < 20; i++) {
            // insert dummy data
            com.nagoya.model.dbo.person.PersonLegal pl = new com.nagoya.model.dbo.person.PersonLegal();
            pl.setEmail("test@test.com" + i);
            pl.setPassword(DefaultPasswordEncryptionProvider.encryptPassword("test@test.com" + i));
            pl.setEmailConfirmed(true);
            pl.setName("test");
            pl.setTaxNumber("test");
            pl.setCommercialRegisterNumber("test");
            personDAO.insert(pl, true);
            if (result == null) {
                result = pl;
            }
        }

        return result;
    }

}
