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

import java.util.Date;

import com.nagoya.common.util.DefaultDateProvider;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.user.RequestType;
import com.nagoya.model.dbo.user.UserRequestDBO;
import com.nagoya.model.exception.BusinessLogicException;

public class UserDeletionService extends DefaultSecureResourceService {

    public UserDeletionService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {
        // we have a valid user
        UserRequestDBO userRequest = new UserRequestDBO();
        PersonDBO person = getOnlineUser().getPerson();
        userRequest.setPerson(person);
        userRequest.setRequestType(RequestType.ACCOUNT_REMOVAL);
        Date expirationDate = DefaultDateProvider.getDeadline24h();
        userRequest.setExpirationDate(expirationDate);
        String token = DefaultIDGenerator.generateRandomID();
        userRequest.setToken(token);
        getPersonDAO().insert(userRequest, true);

        // send confirmation e-mail
        String email = person.getEmail();
        getMailService().sendDeleteAccountMail(email, token, expirationDate);

        return null;
    }

}
