/**
* (C) Copyright 2004 - 2018 CPB Software AG
* 1020 Wien, Vorgartenstrasse 206c
* All rights reserved.
* 
* Created on : Jul 16, 2018
* Created by : flba
*/

package com.nagoya.middleware.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author flba
 *
 */
public class DefaultDateProvider {

    /**
     * 
     * returns the standard deadline starting from now.
     */
    public static Date getDeadline(int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minutes);
        Date dateTime = cal.getTime();
        return dateTime;
    }
    
    /**
     * 
     * returns the standard deadline starting from now.
     */
    public static Date getDeadline24h() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date dateTime = cal.getTime();
        return dateTime;
    }

}
