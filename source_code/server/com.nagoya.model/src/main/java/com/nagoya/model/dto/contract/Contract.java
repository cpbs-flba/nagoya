/**
 * 
 */

package com.nagoya.model.dto.contract;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import com.nagoya.model.to.person.Person;

/**
 * @author Florin Bogdan Balint
 *
 */
@Audited
@Entity(name = "tcontract")
public class Contract implements Serializable {

    private static final long     serialVersionUID  = 1L;

    private Person                sender;

    private Person                receiver;

    private Date                  conclusionDate;

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

    public Date getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(Date conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public Set<ContractResource> getContractResources() {
        return contractResources;
    }

    public void setContractResources(Set<ContractResource> contractResources) {
        this.contractResources = contractResources;
    }

}
