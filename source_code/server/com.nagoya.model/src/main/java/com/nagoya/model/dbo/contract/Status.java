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
     * If a contract is created and the sender has changed his/her mind and cancels this contract, then this status is used.
     */
    CANCELLED,

    /**
     * This status is used, when a contract is expired.
     */
    EXPIRED,

    /**
     * This status is used when a contract is rejected by the receiver.
     */
    REJECTED
}
