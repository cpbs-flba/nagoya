
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
