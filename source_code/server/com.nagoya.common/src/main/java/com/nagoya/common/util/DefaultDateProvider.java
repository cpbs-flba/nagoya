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

package com.nagoya.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        if (StringUtil.isNullOrBlank(dateAsString)) {
            return null;
        }
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

    public static String getCurrentDateAsString() {
        Calendar cal = Calendar.getInstance();
        Date current = cal.getTime();
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = sdf.format(current);
        return result;
    }

    public static String getDateAsString(Date date) {
        if (date == null) {
            return null;
        }
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String result = sdf.format(date);
        return result;
    }

    public static String getDateAsStringSimple(Date date) {
        if (date == null) {
            return null;
        }
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String result = sdf.format(date);
        return result;
    }

}
