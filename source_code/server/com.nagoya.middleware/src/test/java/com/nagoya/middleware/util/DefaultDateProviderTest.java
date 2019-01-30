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

package com.nagoya.middleware.util;

import java.util.Date;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.common.util.DefaultDateProvider;

/**
 * @author Florin Bogdan Balint
 *
 */
public class DefaultDateProviderTest {

    @Test
    @DisplayName("simple positive test")
    public void tp1() {
        Date dateFromString = DefaultDateProvider.getDateFromString("2019-01-16T13:36:07Z");
        Assert.assertNotNull(dateFromString);
    }

    @Test
    @DisplayName("simple negative test")
    public void tp2() {
        Date dateFromString = DefaultDateProvider.getDateFromString("2019-01-16");
        Assert.assertNull(dateFromString);
    }

}
