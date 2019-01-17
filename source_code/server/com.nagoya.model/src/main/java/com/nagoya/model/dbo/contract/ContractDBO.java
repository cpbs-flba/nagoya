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
import com.nagoya.model.dbo.person.PersonDBO;

/**
 * @author Florin Bogdan Balint
 *
 */
@Audited
@Entity(name = "tcontract")
public class ContractDBO extends DBO {

    private static final long        serialVersionUID  = 1L;

    @OneToOne
    @JoinColumn(name = "sender_id")
    private PersonDBO                sender;

    @OneToOne
    @JoinColumn(name = "receiver_id")
    private PersonDBO                receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status                   status;

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private Set<ContractResourceDBO> contractResources = new HashSet<>();

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private Set<ContractFileDBO>     files             = new HashSet<ContractFileDBO>();

    public PersonDBO getSender() {
        return sender;
    }

    public void setSender(PersonDBO sender) {
        this.sender = sender;
    }

    public PersonDBO getReceiver() {
        return receiver;
    }

    public void setReceiver(PersonDBO receiver) {
        this.receiver = receiver;
    }

    public Set<ContractResourceDBO> getContractResources() {
        return contractResources;
    }

    public void setContractResources(Set<ContractResourceDBO> contractResources) {
        this.contractResources = contractResources;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<ContractFileDBO> getFiles() {
        return files;
    }

    public void setFiles(Set<ContractFileDBO> files) {
        this.files = files;
    }

}
