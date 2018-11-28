package com.nagoya.middleware.util;

/**
* (C) Copyright 2004 - 2018 CPB Software AG
* 1020 Wien, Vorgartenstrasse 206c
* All rights reserved.
* 
* Created on : Jul 16, 2018
* Created by : flba
*/


import java.util.UUID;

/**
 * @author flba
 *
 */
public class DefaultIDGenerator {

    /**
     * Generates a default id.
     * 
     * @return
     */
    public static String generateRandomID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
