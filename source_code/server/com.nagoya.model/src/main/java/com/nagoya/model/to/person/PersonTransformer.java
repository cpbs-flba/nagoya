/**
 * 
 */

package com.nagoya.model.to.person;

import java.util.HashSet;
import java.util.Set;

/**
 * @author flba
 *
 */
public final class PersonTransformer {

    private PersonTransformer() {
        // helper class
    }

    public static com.nagoya.model.to.person.PersonTO getDTO(com.nagoya.model.dbo.person.PersonDBO dbo) {
        if (dbo == null) {
            return null;
        }
        com.nagoya.model.to.person.PersonTO result = null;
        if (dbo.getPersonType().equals(com.nagoya.model.dbo.person.PersonType.LEGAL)) {
            result = new com.nagoya.model.to.person.PersonLegalTO();
            getDTO((com.nagoya.model.to.person.PersonLegalTO) result, (com.nagoya.model.dbo.person.PersonLegalDBO) dbo);
        } else {
            result = new com.nagoya.model.to.person.PersonNaturalTO();
            getDTO((com.nagoya.model.to.person.PersonNaturalTO) result, (com.nagoya.model.dbo.person.PersonNaturalDBO) dbo);
        }

        result.setEmail(dbo.getEmail());
        result.setStorePrivateKey(dbo.isStorePrivateKey());
        // ignore the password field
        // result.setPassword(dbo.getPassword());
        result.setAddress(getDTO(dbo.getAddress()));
        Set<com.nagoya.model.to.person.PersonKeysTO> keysToAdd = getDTO(dbo.getKeys());
        result.getKeys().addAll(keysToAdd);

        return result;
    }

    public static Set<com.nagoya.model.to.person.PersonKeysTO> getDTO(Set<com.nagoya.model.dbo.person.PersonKeysDBO> dbo) {
        Set<com.nagoya.model.to.person.PersonKeysTO> result = new HashSet<com.nagoya.model.to.person.PersonKeysTO>();
        if (dbo == null) {
            return result;
        }
        for (com.nagoya.model.dbo.person.PersonKeysDBO dbKey : dbo) {
            com.nagoya.model.to.person.PersonKeysTO toAdd = new com.nagoya.model.to.person.PersonKeysTO();
            toAdd.setPrivateKey(dbKey.getPrivateKey());
            toAdd.setPublicKey(dbKey.getPublicKey());
            result.add(toAdd);
        }
        return result;
    }

    public static com.nagoya.model.to.person.AddressTO getDTO(com.nagoya.model.dbo.person.AddressDBO dbo) {
        if (dbo == null) {
            return null;
        }
        com.nagoya.model.to.person.AddressTO result = new com.nagoya.model.to.person.AddressTO();
        result.setCity(dbo.getCity());
        result.setCountry(dbo.getCountry());
        result.setNumber(dbo.getNumber());
        result.setRegion(dbo.getRegion());
        result.setStreet(dbo.getStreet());
        result.setZip(dbo.getZip());
        return result;
    }

    public static com.nagoya.model.dbo.person.AddressDBO getDBO(com.nagoya.model.dbo.person.AddressDBO dbo,
        com.nagoya.model.to.person.AddressTO dto) {
        if (dto == null) {
            return null;
        }
        if (dbo == null) {
            dbo = new com.nagoya.model.dbo.person.AddressDBO();
        }
        dbo.setCity(dto.getCity());
        dbo.setCountry(dto.getCountry());
        dbo.setNumber(dto.getNumber());
        dbo.setRegion(dto.getRegion());
        dbo.setStreet(dto.getStreet());
        dbo.setZip(dto.getZip());
        return dbo;
    }

    public static com.nagoya.model.to.person.PersonTO getDTO(com.nagoya.model.to.person.PersonNaturalTO dto,
        com.nagoya.model.dbo.person.PersonNaturalDBO dbo) {
        if (dto == null || dbo == null) {
            return null;
        }
        dto.setFirstname(dbo.getFirstname());
        dto.setLastname(dbo.getLastname());
        dto.setBirthdate(dbo.getBirthdate());
        return dto;
    }

    public static com.nagoya.model.to.person.PersonTO getDTO(com.nagoya.model.to.person.PersonLegalTO dto,
        com.nagoya.model.dbo.person.PersonLegalDBO dbo) {
        if (dto == null || dbo == null) {
            return null;
        }
        dto.setCommercialRegisterNumber(dbo.getCommercialRegisterNumber());
        dto.setTaxNumber(dbo.getTaxNumber());
        dto.setName(dbo.getName());
        return dto;
    }

    public static com.nagoya.model.dbo.person.PersonDBO getDBO(com.nagoya.model.dbo.person.PersonDBO dbo, com.nagoya.model.to.person.PersonTO dto) {
        if (dto == null) {
            return null;
        }

        com.nagoya.model.to.person.PersonType personType = dto.getPersonType();
        if (personType == null) {
            return dbo;
        }

        if (dbo == null) {
            if (personType.equals(com.nagoya.model.to.person.PersonType.LEGAL)) {
                dbo = new com.nagoya.model.dbo.person.PersonLegalDBO();
            } else {
                dbo = new com.nagoya.model.dbo.person.PersonNaturalDBO();
            }
        }

        dbo.setEmail(dto.getEmail().toLowerCase());
        dbo.setPassword(dto.getPassword());
        dbo.setStorePrivateKey(dto.isStorePrivateKey());
        dbo.setAddress(getDBO(dbo.getAddress(), dto.getAddress()));

        if (personType.equals(com.nagoya.model.to.person.PersonType.LEGAL)) {
            com.nagoya.model.to.person.PersonLegalTO legalTO = (com.nagoya.model.to.person.PersonLegalTO) dto;
            com.nagoya.model.dbo.person.PersonLegalDBO legal = (com.nagoya.model.dbo.person.PersonLegalDBO) dbo;
            legal.setCommercialRegisterNumber(legalTO.getCommercialRegisterNumber());
            legal.setTaxNumber(legalTO.getTaxNumber());
            legal.setName(legalTO.getName());
            return legal;
        } else {
            com.nagoya.model.to.person.PersonNaturalTO naturalTO = (com.nagoya.model.to.person.PersonNaturalTO) dto;
            com.nagoya.model.dbo.person.PersonNaturalDBO natural = (com.nagoya.model.dbo.person.PersonNaturalDBO) dbo;
            natural.setFirstname(naturalTO.getFirstname());
            natural.setLastname(naturalTO.getLastname());
            natural.setBirthdate(naturalTO.getBirthdate());
            return natural;
        }
    }
}
