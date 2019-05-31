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

package com.nagoya.model.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author flba
 *
 */
public class BusinessLogicException extends Exception {

    private static final long   serialVersionUID = 1L;

    /**
     * HTTP status code.
     */
    private int                 statusCode;

    /**
     * List of business errors.
     */
    private List<BusinessError> errors           = new ArrayList<>();

    public BusinessLogicException(int statusCode) {
        this.statusCode = statusCode;
    }

    public BusinessLogicException(int statusCode, String errorCode, String errorMessage) {
        this.statusCode = statusCode;
        BusinessError businessError = new BusinessError(errorCode, errorMessage);
        this.errors.add(businessError);
    }

    public BusinessLogicException(int statusCode, BusinessError businessError) {
        this.statusCode = statusCode;
        this.errors.add(businessError);
    }

    public BusinessLogicException(int statusCode, List<BusinessError> errors) {
        this.statusCode = statusCode;
        this.errors = errors;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<BusinessError> getErrors() {
        return errors;
    }

    public void setErrors(List<BusinessError> errors) {
        this.errors = errors;
    }

    public void addBusinessError(String errorCode, String errorMessage) {
        BusinessError businessError = new BusinessError(errorCode, errorMessage);
        this.errors.add(businessError);
    }
}
