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

package com.nagoya.model.dbo.person.group;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;

import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.person.PersonDBO;

/**
 * @author Florin Bogdan Balint
 *
 */
@Audited
@Entity(name = "tperson_group")
public class PersonGroupDBO extends DBO {

    private static final long serialVersionUID = 1L;

    @Column(name = "name", nullable = false)
    private String            name;

    @Audited
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tperson_person_group", joinColumns = {
        @JoinColumn(name = "person_id", nullable = false, updatable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "person_group_id", nullable = false, updatable = false) })
    private Set<PersonDBO>    persons          = new HashSet<PersonDBO>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the persons
     */
    public Set<PersonDBO> getPersons() {
        return persons;
    }

    /**
     * @param persons the persons to set
     */
    public void setPersons(Set<PersonDBO> persons) {
        this.persons = persons;
    }

}
