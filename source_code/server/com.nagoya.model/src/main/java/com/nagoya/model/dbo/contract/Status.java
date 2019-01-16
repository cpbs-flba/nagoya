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

/**
 * This represents the status of a contract.
 * 
 * @author Florin Bogdan Balint
 *
 */
public enum Status {

    /**
     * If a contract is in the status 'CREATED' it means the contract has just been created and not yet accepted by the other party.
     */
    CREATED,

    /**
     * A contract which is in this status was accepted by the other party. From a legal perspective the contract is concluded.
     */
    ACCEPTED,

    /**
     * If a contract is cancelled then it means it is no longer valid. Only created contracts (not accepted) can be cancelled.
     */
    CANCELLED
}
