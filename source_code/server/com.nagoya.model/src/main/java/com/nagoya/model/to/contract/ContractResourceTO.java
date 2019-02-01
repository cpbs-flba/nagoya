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

package com.nagoya.model.to.contract;

import java.io.Serializable;

import com.nagoya.model.to.resource.GeneticResourceTO;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractResourceTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private GeneticResourceTO   geneticResource;

    private double            amount;

    private String            measuringUnit;

    public GeneticResourceTO getGeneticResource() {
        return geneticResource;
    }

    public void setGeneticResource(GeneticResourceTO geneticResource) {
        this.geneticResource = geneticResource;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

}
