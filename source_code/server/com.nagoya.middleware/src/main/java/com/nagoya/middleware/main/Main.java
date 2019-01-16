/**
 * (C) Copyright 2004 - 2019 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : flba
 */

package com.nagoya.middleware.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        main.runServer(args);
    }

}
