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
import java.util.HashSet;
import java.util.Set;

import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.to.person.PersonTO;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractTO implements Serializable {

    private static final long       serialVersionUID  = 1L;

    private String                  id;

    private PersonTO                sender;

    private PersonTO                receiver;

    private String                  conclusionDate;

    private Set<ContractResourceTO> contractResources = new HashSet<>();

    private Set<ContractFileTO>     files             = new HashSet<>();

    private Status                  status;

    private String                  token;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the sender
     */
    public PersonTO getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(PersonTO sender) {
        this.sender = sender;
    }

    /**
     * @return the receiver
     */
    public PersonTO getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(PersonTO receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the conclusionDate
     */
    public String getConclusionDate() {
        return conclusionDate;
    }

    /**
     * @param conclusionDate the conclusionDate to set
     */
    public void setConclusionDate(String conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    /**
     * @return the contractResources
     */
    public Set<ContractResourceTO> getContractResources() {
        return contractResources;
    }

    /**
     * @param contractResources the contractResources to set
     */
    public void setContractResources(Set<ContractResourceTO> contractResources) {
        this.contractResources = contractResources;
    }

    /**
     * @return the files
     */
    public Set<ContractFileTO> getFiles() {
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(Set<ContractFileTO> files) {
        this.files = files;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

}
