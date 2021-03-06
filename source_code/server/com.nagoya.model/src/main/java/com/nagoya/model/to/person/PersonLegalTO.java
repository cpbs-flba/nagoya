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

package com.nagoya.model.to.person;

public class PersonLegalTO extends PersonTO {

    private static final long serialVersionUID = 1L;

    private String            name;
    private String            commercialRegisterNumber;
    private String            taxNumber;

    public PersonLegalTO() {
        this.setPersonType(PersonType.LEGAL);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the commercialRegisterNumber
     */
    public String getCommercialRegisterNumber() {
        return commercialRegisterNumber;
    }

    /**
     * @param commercialRegisterNumber the commercialRegisterNumber to set
     */
    public void setCommercialRegisterNumber(String commercialRegisterNumber) {
        this.commercialRegisterNumber = commercialRegisterNumber;
    }

    /**
     * @return the taxNumber
     */
    public String getTaxNumber() {
        return taxNumber;
    }

    /**
     * @param taxNumber the taxNumber to set
     */
    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

}
