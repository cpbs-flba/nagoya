/**
 * (C) Copyright 2004 - 2019 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * Created on : May 29, 2019
 * Created by : flba
 */

package com.nagoya.model.exception;

import java.io.Serializable;

/**
 * @author flba
 *
 */
public class BusinessError implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The error code, that represents a business code for a specific message: E400_INVALID_ACCOUNT
     */
    private String            errorCode;
    /**
     * A corresponding description.
     */
    private String            errorMessage;

    public BusinessError() {
        super();
    }

    public BusinessError(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
