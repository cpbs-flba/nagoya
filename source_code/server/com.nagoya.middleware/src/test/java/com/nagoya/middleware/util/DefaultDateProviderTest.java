/**
 * (C) Copyright 2004 - 2018 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : Florin Bogdan Balint
 */

package com.nagoya.middleware.util;

import java.util.Date;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
