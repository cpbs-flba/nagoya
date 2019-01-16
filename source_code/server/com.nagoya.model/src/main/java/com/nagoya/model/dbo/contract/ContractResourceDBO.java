/**
 * (C) Copyright 2004 - 2018 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : Florin Bogdan Balint
 */

package com.nagoya.model.dbo.contract;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;

/**
 * A contract is concluded for one or more resources. This class represents the connection in between.
 * 
 * @author flba
 *
 */
@Audited
@Entity(name = "tcontract_resource")
public class ContractResourceDBO extends DBO {

    private static final long serialVersionUID = 1L;

    @NotAudited
    @OneToOne
    @JoinColumn(name = "genetic_resource_id")
    private GeneticResourceDBO   geneticResource;

    @Column(name = "amount")
    private BigDecimal        amount;

    @Column(name = "measuring_unit")
    private String            measuringUnit;

    public GeneticResourceDBO getGeneticResource() {
        return geneticResource;
    }

    public void setGeneticResource(GeneticResourceDBO geneticResource) {
        this.geneticResource = geneticResource;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

}
