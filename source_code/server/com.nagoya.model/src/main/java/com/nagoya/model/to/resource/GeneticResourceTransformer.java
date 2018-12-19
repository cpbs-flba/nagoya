/**
 * 
 */
package com.nagoya.model.to.resource;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Florin Bogdan Balint
 *
 */
public final class GeneticResourceTransformer {

	private GeneticResourceTransformer() {
		// noop
	}

	public static com.nagoya.model.dbo.resource.GeneticResource getDBO(
			com.nagoya.model.dbo.resource.GeneticResource dbo, com.nagoya.model.to.resource.GeneticResource dto) {
		if (dto == null) {
			return null;
		}
		if (dbo == null) {
			dbo = new com.nagoya.model.dbo.resource.GeneticResource();
		}
		dbo.setId(dto.getId());
		dbo.setDescription(dto.getDescription());
		dbo.setHashSequence(dto.getHashSequence());
		dbo.setOrigin(dto.getOrigin());
		dbo.setSource(dto.getSource());
		dbo.setVisibilityType(dto.getVisibilityType());
		dbo.setIdentifier(dto.getIdentifier());
		dbo.setTaxonomy(getDBO(dbo.getTaxonomy(), dto.getTaxonomy()));
		dbo.getFiles().addAll(getDBO(dto.getFiles()));
		return dbo;
	}

	public static Set<com.nagoya.model.dbo.resource.ResourceFile> getDBO(
			Set<com.nagoya.model.to.resource.ResourceFile> dto) {
		if (dto == null) {
			return null;
		}
		Set<com.nagoya.model.dbo.resource.ResourceFile> result = new HashSet<>();
		for (com.nagoya.model.to.resource.ResourceFile resourceFile : dto) {
			result.add(getDBO(resourceFile));
		}
		return result;
	}

	public static com.nagoya.model.to.resource.Taxonomy getDTO(com.nagoya.model.to.resource.Taxonomy dto,
			com.nagoya.model.dbo.resource.Taxonomy dbo) {
		if (dbo == null) {
			return null;
		}
		if (dto == null) {
			dto = new com.nagoya.model.to.resource.Taxonomy();
		}
		dto.setKingdom(dbo.getKingdom());
		dto.setPhylum(dbo.getPhylum());
		dto.setClazz(dbo.get_class());
		dto.setOrder(dbo.getOrder());
		dto.setFamily(dbo.getFamily());
		dto.setSubFamily(dbo.getSubFamily());
		dto.setSuperTribe(dbo.getSuperTribe());
		dto.setTribe(dbo.getTribe());
		dto.setGenus(dbo.getGenus());
		dto.setSpecies(dbo.getSpecies());
		return dto;
	}

	public static com.nagoya.model.dbo.resource.Taxonomy getDBO(com.nagoya.model.dbo.resource.Taxonomy dbo,
			com.nagoya.model.to.resource.Taxonomy dto) {
		if (dto == null) {
			return null;
		}
		if (dbo == null) {
			dbo = new com.nagoya.model.dbo.resource.Taxonomy();
		}
		dbo.setKingdom(dto.getKingdom());
		dbo.setPhylum(dto.getPhylum());
		dbo.set_class(dto.getClazz());
		dbo.setOrder(dto.getOrder());
		dbo.setFamily(dto.getFamily());
		dbo.setSubFamily(dto.getSubFamily());
		dbo.setSuperTribe(dto.getSuperTribe());
		dbo.setTribe(dto.getTribe());
		dbo.setGenus(dto.getGenus());
		dbo.setSpecies(dto.getSpecies());
		return dbo;
	}

	public static com.nagoya.model.dbo.resource.ResourceFile getDBO(com.nagoya.model.to.resource.ResourceFile dto) {
		if (dto == null) {
			return null;
		}
		com.nagoya.model.dbo.resource.ResourceFile result = new com.nagoya.model.dbo.resource.ResourceFile();
		result.setId(dto.getId());
		result.setName(dto.getName());
		result.setType(dto.getType());
		result.setContent(dto.getContent());
		return result;
	}

	public static com.nagoya.model.to.resource.GeneticResource getDTO(
			com.nagoya.model.dbo.resource.GeneticResource dbo) {
		if (dbo == null) {
			return null;
		}
		com.nagoya.model.to.resource.GeneticResource result = new com.nagoya.model.to.resource.GeneticResource();
		result.setId(dbo.getId());
		result.setDescription(dbo.getDescription());
		result.setHashSequence(dbo.getHashSequence());
		result.setOrigin(dbo.getOrigin());
		result.setSource(dbo.getSource());
		result.setVisibilityType(dbo.getVisibilityType());
		result.setIdentifier(dbo.getIdentifier());
		result.setFiles(getDTO(dbo.getFiles()));
		result.setTaxonomy(getDTO(result.getTaxonomy(), dbo.getTaxonomy()));
		return result;
	}

	public static Set<com.nagoya.model.to.resource.ResourceFile> getDTO(
			Set<com.nagoya.model.dbo.resource.ResourceFile> dbo) {
		if (dbo == null) {
			return null;
		}
		Set<com.nagoya.model.to.resource.ResourceFile> result = new HashSet<>();
		for (com.nagoya.model.dbo.resource.ResourceFile resourceFile : dbo) {
			result.add(getDTO(resourceFile));
		}
		return result;
	}

	public static com.nagoya.model.to.resource.ResourceFile getDTO(com.nagoya.model.dbo.resource.ResourceFile dbo) {
		if (dbo == null) {
			return null;
		}
		com.nagoya.model.to.resource.ResourceFile result = new com.nagoya.model.to.resource.ResourceFile();
		result.setId(dbo.getId());
		result.setName(dbo.getName());
		result.setType(dbo.getType());
		result.setContent(dbo.getContent());
		return result;
	}

}
