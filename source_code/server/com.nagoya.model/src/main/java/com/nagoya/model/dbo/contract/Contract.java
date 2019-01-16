/**
 * (C) Copyright 2004 - 2019 CPB Software AG
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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.person.Person;

/**
 * @author Florin Bogdan Balint
 *
 */
@Audited
@Entity(name = "tcontract")
public class Contract extends DBO {

    private static final long     serialVersionUID  = 1L;

    @OneToOne
    @JoinColumn(name = "sender_id")
    private Person                sender;

    @OneToOne
    @JoinColumn(name = "receiver_id")
    private Person                receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status                status;

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private Set<ContractResource> contractResources = new HashSet<>();

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Person getReceiver() {
        return receiver;
    }

    public void setReceiver(Person receiver) {
        this.receiver = receiver;
    }

    public Set<ContractResource> getContractResources() {
        return contractResources;
    }

    public void setContractResources(Set<ContractResource> contractResources) {
        this.contractResources = contractResources;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
