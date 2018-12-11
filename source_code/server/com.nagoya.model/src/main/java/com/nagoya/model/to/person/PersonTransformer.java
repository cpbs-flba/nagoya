/**
 * 
 */
package com.nagoya.model.to.person;

import java.util.HashSet;
import java.util.Set;

import com.nagoya.model.dbo.person.PersonType;

/**
 * @author flba
 *
 */
public final class PersonTransformer {

	private PersonTransformer() {
		// helper class
	}

	public static com.nagoya.model.to.person.Person getDTO(com.nagoya.model.dbo.person.Person dbo) {
		if (dbo == null) {
			return null;
		}
		com.nagoya.model.to.person.Person result = null;
		if (dbo.getPersonType().equals(PersonType.LEGAL)) {
			result = new com.nagoya.model.to.person.PersonLegal();
			getDTO((com.nagoya.model.to.person.PersonLegal) result, (com.nagoya.model.dbo.person.PersonLegal) dbo);
		} else {
			result = new com.nagoya.model.to.person.PersonNatural();
			getDTO((com.nagoya.model.to.person.PersonNatural) result, (com.nagoya.model.dbo.person.PersonNatural) dbo);
		}

		result.setEmail(dbo.getEmail());
		result.setPassword(dbo.getPassword());
		result.setPersonType(dbo.getPersonType());
		result.setAddress(getDTO(dbo.getAddress()));
		result.setKeys(getDTO(dbo.getKeys()));

		return result;
	}

	public static Set<com.nagoya.model.to.person.PersonKeys> getDTO(Set<com.nagoya.model.dbo.person.PersonKeys> dbo) {
		if (dbo == null) {
			return null;
		}
		Set<com.nagoya.model.to.person.PersonKeys> result = new HashSet<com.nagoya.model.to.person.PersonKeys>();
		for (com.nagoya.model.dbo.person.PersonKeys dbKey : dbo) {
			com.nagoya.model.to.person.PersonKeys toAdd = new com.nagoya.model.to.person.PersonKeys();
			toAdd.setPrivateKey(dbKey.getPrivateKey());
			toAdd.setPublicKey(dbKey.getPublicKey());
			result.add(toAdd);
		}
		return result;
	}

	public static com.nagoya.model.to.person.Address getDTO(com.nagoya.model.dbo.person.Address dbo) {
		if (dbo == null) {
			return null;
		}
		com.nagoya.model.to.person.Address result = new com.nagoya.model.to.person.Address();
		result.setCity(dbo.getCity());
		result.setCountry(dbo.getCountry());
		result.setNumber(dbo.getNumber());
		result.setRegion(dbo.getRegion());
		result.setStreet(dbo.getStreet());
		result.setZip(dbo.getZip());
		return result;
	}

	public static com.nagoya.model.dbo.person.Address getDBO(com.nagoya.model.dbo.person.Address dbo,
			com.nagoya.model.to.person.Address dto) {
		if (dto == null) {
			return null;
		}
		if (dbo == null) {
			dbo = new com.nagoya.model.dbo.person.Address();
		}
		dbo.setCity(dto.getCity());
		dbo.setCountry(dto.getCountry());
		dbo.setNumber(dto.getNumber());
		dbo.setRegion(dto.getRegion());
		dbo.setStreet(dto.getStreet());
		dbo.setZip(dto.getZip());
		return dbo;
	}

	public static com.nagoya.model.to.person.Person getDTO(com.nagoya.model.to.person.PersonNatural dto,
			com.nagoya.model.dbo.person.PersonNatural dbo) {
		if (dto == null || dbo == null) {
			return null;
		}
		dto.setFirstname(dbo.getFirstname());
		dto.setLastname(dbo.getLastname());
		dto.setBirthdate(dbo.getBirthdate());
		return dto;
	}

	public static com.nagoya.model.to.person.Person getDTO(com.nagoya.model.to.person.PersonLegal dto,
			com.nagoya.model.dbo.person.PersonLegal dbo) {
		if (dto == null || dbo == null) {
			return null;
		}
		dto.setCommercialRegisterNumber(dbo.getCommercialRegisterNumber());
		dto.setTaxNumber(dbo.getTaxNumber());
		dto.setName(dbo.getName());
		return dto;
	}

	public static com.nagoya.model.dbo.person.Person getDBO(com.nagoya.model.dbo.person.Person dbo,
			com.nagoya.model.to.person.Person dto) {
		if (dto == null) {
			return null;
		}
		if (dbo == null) {
			dbo = new com.nagoya.model.dbo.person.Person();
		}
		dbo.setEmail(dto.getEmail());
		dbo.setPassword(dto.getPassword());
		dbo.setAddress(getDBO(dbo.getAddress(), dto.getAddress()));
		PersonType personType = dto.getPersonType();
		dbo.setPersonType(personType);
		if (personType == null) {
			return dbo;
		}
		if (personType.equals(PersonType.LEGAL)) {
			com.nagoya.model.to.person.PersonLegal legalTO = (com.nagoya.model.to.person.PersonLegal) dto;
			com.nagoya.model.dbo.person.PersonLegal legal = new com.nagoya.model.dbo.person.PersonLegal(dbo);
			legal.setName(legalTO.getName());
			legal.setCommercialRegisterNumber(legalTO.getCommercialRegisterNumber());
			legal.setTaxNumber(legalTO.getTaxNumber());
			return legal;
		} else {
			com.nagoya.model.to.person.PersonNatural naturalTO = (com.nagoya.model.to.person.PersonNatural) dto;
			com.nagoya.model.dbo.person.PersonNatural natural = new com.nagoya.model.dbo.person.PersonNatural(dbo);
			natural.setFirstname(naturalTO.getFirstname());
			natural.setLastname(naturalTO.getLastname());
			natural.setBirthdate(naturalTO.getBirthdate());
			return natural;
		}
	}
}
