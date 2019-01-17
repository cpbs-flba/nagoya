/**
 * 
 */

package com.nagoya.model.to.contract;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.nagoya.model.to.person.PersonTO;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractTO implements Serializable {

    private static final long       serialVersionUID  = 1L;

    private PersonTO                sender;

    private PersonTO                receiver;

    private Date                    conclusionDate;

    private Set<ContractResourceTO> contractResources = new HashSet<>();

    private Set<ContractResourceTO> files             = new HashSet<>();

    public PersonTO getSender() {
        return sender;
    }

    public void setSender(PersonTO sender) {
        this.sender = sender;
    }

    public PersonTO getReceiver() {
        return receiver;
    }

    public void setReceiver(PersonTO receiver) {
        this.receiver = receiver;
    }

    public Date getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(Date conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public Set<ContractResourceTO> getContractResources() {
        return contractResources;
    }

    public void setContractResources(Set<ContractResourceTO> contractResources) {
        this.contractResources = contractResources;
    }

    public Set<ContractResourceTO> getFiles() {
        return files;
    }

    public void setFiles(Set<ContractResourceTO> files) {
        this.files = files;
    }

}
