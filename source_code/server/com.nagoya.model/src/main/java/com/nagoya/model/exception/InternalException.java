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

/**
 * @author flba
 *
 */
public class InternalException extends java.lang.RuntimeException {

    private static final long serialVersionUID = 1L;

    public InternalException() {
        // empty
    }

    public InternalException(String errorMessage) {
        super(errorMessage);
    }

    public InternalException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public InternalException(Throwable e) {
        super(e);
    }

}
