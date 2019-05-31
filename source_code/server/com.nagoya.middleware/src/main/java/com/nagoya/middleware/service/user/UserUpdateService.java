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

import com.nagoya.common.crypto.DefaultPasswordEncryptionProvider;
import com.nagoya.common.util.StringUtil;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.to.person.PersonTO;
import com.nagoya.model.to.person.PersonTransformer;

public class UserUpdateService extends DefaultSecureResourceService {

    public UserUpdateService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {

        PersonTO personTO = (PersonTO) params[0];

        // all updates must be confirmed via password confirmation
        PersonDBO person = getOnlineUser().getPerson();
        String actualPassword = person.getPassword();
        String passwordConfirmation = personTO.getPasswordConfirmation();
        if (!DefaultPasswordEncryptionProvider.isPasswordCorrect(actualPassword, passwordConfirmation)) {
            throw new BusinessLogicException(401, "E401_INVALID_PW", "Invalid PW");
        }

        String newPassword = personTO.getPassword();
        if (StringUtil.isNotNullOrBlank(newPassword)) {
            String encryptPassword = DefaultPasswordEncryptionProvider.encryptPassword(newPassword);
            personTO.setPassword(encryptPassword);
        } else {
            personTO.setPassword(person.getPassword());
        }
        person = PersonTransformer.getDBO(person, personTO);
        try {
            getPersonDAO().update(person, true);
        } catch (InvalidObjectException | ResourceOutOfDateException e) {
            throw new InternalException(e);
        }

        DefaultResponse result = new DefaultResponse();
        PersonTO dto = PersonTransformer.getDTO(person);
        result.setEntity(dto);
        return result;
    }

}
