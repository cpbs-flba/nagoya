/**
* (C) Copyright 2004 - 2018 CPB Software AG
* 1020 Wien, Vorgartenstrasse 206c
* All rights reserved.
* 
* Created on : Jul 16, 2018
* Created by : flba
*/

package com.nagoya.middleware.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /**
     * Retrieves the date from an ISO 8601 pattern with the following format: <br>
     * yyyy-MM-dd'T'HH:mm:ss'Z' <br>
     * Example: 2019-01-16T13:36:07Z
     * 
     * @param dateAsString
     * @return java.util.Date date
     */
    public static Date getDateFromString(String dateAsString) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date parsedDate = sdf.parse(dateAsString);
            return parsedDate;
        } catch (ParseException e) {
            // either it works or not...
        }
        return null;
    }

}
