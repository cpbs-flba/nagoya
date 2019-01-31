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

package com.nagoya.middleware.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nagoya.middleware.rest.bl.impl.BlockchainSearchRESTResourceImpl;
import com.nagoya.middleware.rest.bl.impl.ContractRESTResourceImpl;
import com.nagoya.middleware.rest.bl.impl.GeneticRESTResourceImpl;
import com.nagoya.middleware.rest.bl.impl.UserRESTResourceImpl;

/**
 * @author flba
 *
 */
public class Main extends Microservice {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String args[])
        throws Exception {
        LOGGER.debug("Starting program.");

        Main main = new Main();

        // add the REST resources here
        main.getResourceConfig().register(UserRESTResourceImpl.class);
        main.getResourceConfig().register(GeneticRESTResourceImpl.class);
        main.getResourceConfig().register(ContractRESTResourceImpl.class);
        main.getResourceConfig().register(BlockchainSearchRESTResourceImpl.class);

        main.runServer(args);
    }

}
