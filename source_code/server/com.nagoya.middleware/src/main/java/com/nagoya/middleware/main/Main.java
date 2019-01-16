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

import com.nagoya.middleware.rest.bl.impl.ContractResourceImpl;
import com.nagoya.middleware.rest.bl.impl.GeneticResourceImpl;
import com.nagoya.middleware.rest.bl.impl.UserResourceImpl;

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
        main.getResourceConfig().register(UserResourceImpl.class);
        main.getResourceConfig().register(GeneticResourceImpl.class);
        main.getResourceConfig().register(ContractResourceImpl.class);

        main.runServer(args);
    }

}
