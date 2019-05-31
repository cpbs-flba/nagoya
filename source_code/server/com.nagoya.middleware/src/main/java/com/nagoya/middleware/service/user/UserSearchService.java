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

package com.nagoya.middleware.service.user;

import java.util.ArrayList;
import java.util.List;

import com.nagoya.common.util.StringUtil;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.person.PersonNaturalDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.to.person.PersonTO;
import com.nagoya.model.to.person.PersonTransformer;

public class UserSearchService extends DefaultSecureResourceService {

    public UserSearchService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {

        String personFilter = (String) params[0];
        if (StringUtil.isNullOrBlank(personFilter)) {
            throw new BusinessLogicException(400, "E400_INVALID_SEARCH_FILTER", "the filter must contain at least one character.");
        }

        List<com.nagoya.model.to.person.PersonTO> resultsTO = new ArrayList<>();
        List<PersonNaturalDBO> searchNatural = getPersonDAO().searchNatural(personFilter, 10);
        for (PersonNaturalDBO personDBO : searchNatural) {
            PersonTO dto = PersonTransformer.getDTO(personDBO);
            resultsTO.add(dto);
        }

        List<PersonLegalDBO> searchLegal = getPersonDAO().searchLegal(personFilter, 10);
        for (PersonLegalDBO personDBO : searchLegal) {
            PersonTO dto = PersonTransformer.getDTO(personDBO);
            resultsTO.add(dto);
        }

        DefaultResponse result = new DefaultResponse();
        result.setEntity(resultsTO);
        return result;
    }

}
