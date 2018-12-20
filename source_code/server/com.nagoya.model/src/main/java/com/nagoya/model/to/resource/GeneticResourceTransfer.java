/**
 * 
 */
package com.nagoya.model.to.resource;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.nagoya.model.to.person.Person;


/**
 * @author Florin Bogdan Balint
 */
public class GeneticResourceTransfer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Person sender;
	private Person receiver;
	private boolean receiverAcceptedTransfer;
	private Set<GeneticResource> geneticResources = new HashSet<>();

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

	public boolean isReceiverAcceptedTransfer() {
		return receiverAcceptedTransfer;
	}

	public void setReceiverAcceptedTransfer(boolean receiverAcceptedTransfer) {
		this.receiverAcceptedTransfer = receiverAcceptedTransfer;
	}

	public Set<GeneticResource> getGeneticResources() {
		return geneticResources;
	}

	public void setGeneticResources(Set<GeneticResource> geneticResources) {
		this.geneticResources = geneticResources;
	}

}
