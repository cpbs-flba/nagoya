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

package com.nagoya.dao.util;

import java.util.Calendar;
import java.util.Date;

import com.nagoya.common.util.StringUtil;
import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.SimpleDBO;

public final class DefaultDBOFiller {

    private DefaultDBOFiller() {
        // defeat instantiation
    }

    public static void fillDefaultDataObjectValues(SimpleDBO simpleDBO) {
        if (simpleDBO instanceof DBO == false) {
            return;
        }
        DBO dbo = (DBO) simpleDBO;
        fillDefaultDataObjectValues(dbo);
    }

    public static void fillDefaultDataObjectValues(DBO dbo) {
        Date dateTime = Calendar.getInstance().getTime();

        if (StringUtil.isNullOrBlank(dbo.getCreationUser())) {
            dbo.setCreationUser("auto");
        }

        if (dbo.getCreationDate() == null) {
            dbo.setCreationDate(dateTime);
        }

        if (StringUtil.isNullOrBlank(dbo.getModificationUser())) {
            dbo.setModificationUser("auto");
        }

        dbo.setModificationDate(dateTime);
    }

}
