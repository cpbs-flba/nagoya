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
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.nagoya.model.blockchain.Credentials;
import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.user.UserRequestDBO;

/**
 * @author Florin Bogdan Balint
 *
 */
@Audited
@Entity(name = "tcontract")
public class ContractDBO extends DBO {

    private static final long        serialVersionUID  = 1L;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private PersonDBO                sender;

    @OneToOne(fetch = FetchType.EAGER)
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
    private Set<ContractFileDBO>     files             = new HashSet<>();

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private Set<UserRequestDBO>      userRequests      = new HashSet<>();

    @Transient
    private Credentials              credentialsSender;

    @Transient
    private Credentials              credentialsReceiver;

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

    /**
     * @return the userRequests
     */
    public Set<UserRequestDBO> getUserRequests() {
        return userRequests;
    }

    /**
     * @param userRequests the userRequests to set
     */
    public void setUserRequests(Set<UserRequestDBO> userRequests) {
        this.userRequests = userRequests;
    }

    /**
     * @return the credentialsSender
     */
    public Credentials getCredentialsSender() {
        return credentialsSender;
    }

    /**
     * @param credentialsSender the credentialsSender to set
     */
    public void setCredentialsSender(Credentials credentialsSender) {
        this.credentialsSender = credentialsSender;
    }

    /**
     * @return the credentialsReceiver
     */
    public Credentials getCredentialsReceiver() {
        return credentialsReceiver;
    }

    /**
     * @param credentialsReceiver the credentialsReceiver to set
     */
    public void setCredentialsReceiver(Credentials credentialsReceiver) {
        this.credentialsReceiver = credentialsReceiver;
    }

}
