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

package com.nagoya.model.dbo.person;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.envers.Audited;

@Audited
@Entity(name = "tperson_natural")
@PrimaryKeyJoinColumn(name = "person_id")
public class PersonNaturalDBO extends PersonDBO {

    private static final long serialVersionUID = 1L;

    @Column(name = "first_name")
    private String            firstname;

    @Column(name = "last_name")
    private String            lastname;

    @Column(name = "birthdate")
    private Date              birthdate;

    public PersonNaturalDBO() {
        this.setPersonType(PersonType.NATURAL);
    }

    public PersonNaturalDBO(PersonDBO person) {
        this.setPersonType(PersonType.NATURAL);
        this.setEmail(person.getEmail());
        this.setPassword(person.getPassword());
        this.setAddress(person.getAddress());
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the birthdate
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * @param birthdate the birthdate to set
     */
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

}
