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

package com.nagoya.model.dbo.resource;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.envers.NotAudited;

import com.nagoya.model.dbo.SimpleDBO;

/**
 * 
 * @author Florin Bogdan Balint
 *
 */
@Entity(name = "tgenetic_resource_taxonomy")
public class TaxonomyDBO extends SimpleDBO {

    private static final long serialVersionUID = 1L;

    @NotAudited
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private TaxonomyDBO       parent;

    @Column(name = "name")
    private String            name;

    /**
     * @return the parent
     */
    public TaxonomyDBO getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(TaxonomyDBO parent) {
        this.parent = parent;
    }

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String text = "";
        if (parent != null) {
            text += parent.toString() + " > ";
        }
        text += name;
        return text;
    }

}
