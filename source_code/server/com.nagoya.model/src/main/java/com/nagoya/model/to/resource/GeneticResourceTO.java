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

package com.nagoya.model.to.resource;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nagoya.model.dbo.resource.VisibilityType;

/**
 * @author Florin Bogdan Balint
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneticResourceTO implements Serializable {

    private static final long   serialVersionUID = 1L;

    private Long                id;
    private String              identifier;
    private String              description;
    private String              origin;
    private String              source;
    private String              hashSequence;
    private VisibilityType      visibilityType;
    private TaxonomyTO          taxonomy;
    private Set<ResourceFileTO> files            = new HashSet<ResourceFileTO>();

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the hashSequence
     */
    public String getHashSequence() {
        return hashSequence;
    }

    /**
     * @param hashSequence the hashSequence to set
     */
    public void setHashSequence(String hashSequence) {
        this.hashSequence = hashSequence;
    }

    /**
     * @return the visibilityType
     */
    public VisibilityType getVisibilityType() {
        return visibilityType;
    }

    /**
     * @param visibilityType the visibilityType to set
     */
    public void setVisibilityType(VisibilityType visibilityType) {
        this.visibilityType = visibilityType;
    }

    /**
     * @return the files
     */
    public Set<ResourceFileTO> getFiles() {
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(Set<ResourceFileTO> files) {
        this.files = files;
    }

    /**
     * @return the taxonomy
     */
    public TaxonomyTO getTaxonomy() {
        return taxonomy;
    }

    /**
     * @param taxonomy the taxonomy to set
     */
    public void setTaxonomy(TaxonomyTO taxonomy) {
        this.taxonomy = taxonomy;
    }

}
