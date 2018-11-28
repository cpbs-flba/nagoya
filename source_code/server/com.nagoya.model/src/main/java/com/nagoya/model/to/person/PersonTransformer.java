/**
 * 
 */
package com.nagoya.model.to.person;

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
		com.nagoya.model.to.person.Person result = new com.nagoya.model.to.person.Person();
		result.setEmail(dbo.getEmail());
		result.setPassword(dbo.getPassword());
		result.setPersonType(dbo.getPersonType());
		return result;
	}

	public static com.nagoya.model.dbo.person.Person getDBO(com.nagoya.model.to.person.Person dto) {
		if (dto == null) {
			return null;
		}
		com.nagoya.model.dbo.person.Person result = new com.nagoya.model.dbo.person.Person();
		result.setEmail(dto.getEmail());
		result.setPassword(dto.getPassword());
		PersonType personType = dto.getPersonType();
		result.setPersonType(personType);
		if (personType == null) {
			return result;
		}
		if (personType.equals(PersonType.LEGAL)) {
			com.nagoya.model.to.person.PersonLegal legalTO = (com.nagoya.model.to.person.PersonLegal) dto;
			com.nagoya.model.dbo.person.PersonLegal legal = new com.nagoya.model.dbo.person.PersonLegal(result);
			legal.setName(legalTO.getName());
			legal.setCommercialRegisterNumber(legalTO.getCommercialRegisterNumber());
			legal.setTaxNumber(legalTO.getTaxNumber());
			return legal;
		} else {
			com.nagoya.model.to.person.PersonNatural naturalTO = (com.nagoya.model.to.person.PersonNatural) dto;
			com.nagoya.model.dbo.person.PersonNatural natural = new com.nagoya.model.dbo.person.PersonNatural(result);
			natural.setFirstname(naturalTO.getFirstname());
			natural.setLastname(naturalTO.getLastname());
			natural.setBirthdate(naturalTO.getBirthdate());
			return natural;
		}
	}
}
