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

import com.nagoya.middleware.test.base.RestBaseTest;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.to.contract.ContractResourceTO;
import com.nagoya.model.to.contract.ContractTO;
import com.nagoya.model.to.person.PersonTO;

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
        PersonLegalDBO p1 = insertDummyLegalPerson();

        // insert the genetic resource
        GeneticResourceDBO insertedGeneticResource = insertTestGeneticResource(getSession(), p1, "bla123", VisibilityType.PRIVATE,
            getTaxonomyParent());

        // now login
        String targetUrl = serverURL + "/users/login";
        LOGGER.debug("Sending request POST: " + targetUrl);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(targetUrl);

        com.nagoya.model.to.person.PersonLegalTO personTO = new com.nagoya.model.to.person.PersonLegalTO();
        personTO.setEmail("test@test.com1");
        personTO.setPassword("test@test.com1");
        Entity<com.nagoya.model.to.person.PersonLegalTO> entity = Entity.entity(personTO, MediaType.APPLICATION_JSON);

        Response response = target.request(MediaType.APPLICATION_JSON).post(entity);
        String authHeader = response.getHeaderString("Authorization");
        int status = response.getStatus();
        response.close();
        Assert.assertEquals(200, status);

        // now create a contract
        PersonTO receiver = new PersonTO();
        receiver.setEmail("test@test.com2");

        ContractTO contractTO = new ContractTO();
        contractTO.setReceiver(receiver);

        ContractResourceTO cr1 = new ContractResourceTO();
        cr1.setAmount(100);
        com.nagoya.model.to.resource.GeneticResourceTO geneticResource = new com.nagoya.model.to.resource.GeneticResourceTO();
        geneticResource.setId(insertedGeneticResource.getId());
        cr1.setGeneticResource(geneticResource);
        cr1.setMeasuringUnit("unit");
        contractTO.getContractResources().add(cr1);

        // now lets put this contract
        targetUrl = serverURL + "/contracts";
        client = ClientBuilder.newClient();
        target = client.target(targetUrl);
        Entity<ContractTO> entity2 = Entity.entity(contractTO, MediaType.APPLICATION_JSON);
        response = target //
            .request(MediaType.APPLICATION_JSON)//
            .header("Language", "de") //
            .header("Authorization", authHeader) //
            .put(entity2);
        status = response.getStatus();
        response.close();

        Assert.assertEquals(204, status);
    }

    private PersonLegalDBO insertDummyLegalPerson() {
        PersonLegalDBO result = null;
        // save the legal person
        for (int i = 0; i < 3; i++) {
            String email = "test@test.com" + i;
            PersonLegalDBO insertTestPersonLegal = insertTestPersonLegal(getSession(), email);
            if (result == null) {
                result = insertTestPersonLegal;
            }
        }
        return result;

    }

}
