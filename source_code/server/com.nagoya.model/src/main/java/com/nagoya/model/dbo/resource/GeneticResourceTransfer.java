/**
 * 
 */
package com.nagoya.model.dbo.resource;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.person.Person;

/**
 * @author Florin Bogdan Balint
 */
@Audited
@Entity(name = "tgenetic_resource_transfer")
public class GeneticResourceTransfer extends DBO {

	private static final long serialVersionUID = 1L;

	@NotAudited
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sender_id")
	private Person sender;

	@NotAudited
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "receiver_id")
	private Person receiver;

	@Column(name = "receiver_accepted_transfer", nullable = false)
	private boolean receiverAcceptedTransfer;

	@Column(name = "persisted_in_blockchain", nullable = false)
	private boolean persistedInBlockChain;

	@NotAudited
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "tgenetic_resource_transfer_resources", //
			joinColumns = @JoinColumn(name = "genetic_resource_transfer_id", referencedColumnName = "id"), //
			inverseJoinColumns = @JoinColumn(name = "genetic_resource_id", referencedColumnName = "id")//
	)
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

	public boolean isPersistedInBlockChain() {
		return persistedInBlockChain;
	}

	public void setPersistedInBlockChain(boolean persistedInBlockChain) {
		this.persistedInBlockChain = persistedInBlockChain;
	}

	public Set<GeneticResource> getGeneticResources() {
		return geneticResources;
	}

	public void setGeneticResources(Set<GeneticResource> geneticResources) {
		this.geneticResources = geneticResources;
	}

}
