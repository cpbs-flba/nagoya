
package com.nagoya.model.dto.contract;

import java.io.Serializable;

import com.nagoya.model.to.resource.GeneticResource;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private GeneticResource   geneticResource;

    private double            amount;

    private String            measuringUnit;

    public GeneticResource getGeneticResource() {
        return geneticResource;
    }

    public void setGeneticResource(GeneticResource geneticResource) {
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
