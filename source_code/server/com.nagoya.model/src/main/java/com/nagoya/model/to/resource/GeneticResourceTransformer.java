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

	public static com.nagoya.model.dbo.resource.GeneticResourceDBO getDBO(
			com.nagoya.model.dbo.resource.GeneticResourceDBO dbo, com.nagoya.model.to.resource.GeneticResourceTO dto) {
		if (dto == null) {
			return null;
		}
		if (dbo == null) {
			dbo = new com.nagoya.model.dbo.resource.GeneticResourceDBO();
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

	public static Set<com.nagoya.model.dbo.resource.ResourceFileDBO> getDBO(
			Set<com.nagoya.model.to.resource.ResourceFileTO> dto) {
		if (dto == null) {
			return null;
		}
		Set<com.nagoya.model.dbo.resource.ResourceFileDBO> result = new HashSet<>();
		for (com.nagoya.model.to.resource.ResourceFileTO resourceFile : dto) {
			result.add(getDBO(resourceFile));
		}
		return result;
	}

	public static com.nagoya.model.to.resource.TaxonomyTO getDTO(com.nagoya.model.to.resource.TaxonomyTO dto,
			com.nagoya.model.dbo.resource.TaxonomyDBO dbo) {
		if (dbo == null) {
			return null;
		}
		if (dto == null) {
			dto = new com.nagoya.model.to.resource.TaxonomyTO();
		}
		dto.setId(dbo.getId().toString());
		dto.setName(dbo.getName());
		if (dbo.getParent() != null) {
			dto.setParent(getDTO(dto.getParent(), dbo.getParent()));
		}
		return dto;
	}

	public static com.nagoya.model.dbo.resource.TaxonomyDBO getDBO(com.nagoya.model.dbo.resource.TaxonomyDBO dbo,
			com.nagoya.model.to.resource.TaxonomyTO dto) {
		if (dto == null) {
			return null;
		}
		if (dbo == null) {
			dbo = new com.nagoya.model.dbo.resource.TaxonomyDBO();
		}
		dbo.setName(dto.getName());
		if (dto.getParent() != null) {
			dbo.setParent(getDBO(dbo.getParent(), dto.getParent()));
		}
		return dbo;
	}

	public static com.nagoya.model.dbo.resource.ResourceFileDBO getDBO(com.nagoya.model.to.resource.ResourceFileTO dto) {
		if (dto == null) {
			return null;
		}
		com.nagoya.model.dbo.resource.ResourceFileDBO result = new com.nagoya.model.dbo.resource.ResourceFileDBO();
		result.setId(dto.getId());
		result.setName(dto.getName());
		result.setType(dto.getType());
		result.setContent(dto.getContent());
		return result;
	}

	public static com.nagoya.model.to.resource.GeneticResourceTO getDTO(
			com.nagoya.model.dbo.resource.GeneticResourceDBO dbo) {
		if (dbo == null) {
			return null;
		}
		com.nagoya.model.to.resource.GeneticResourceTO result = new com.nagoya.model.to.resource.GeneticResourceTO();
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

	public static Set<com.nagoya.model.to.resource.ResourceFileTO> getDTO(
			Set<com.nagoya.model.dbo.resource.ResourceFileDBO> dbo) {
		if (dbo == null) {
			return null;
		}
		Set<com.nagoya.model.to.resource.ResourceFileTO> result = new HashSet<>();
		for (com.nagoya.model.dbo.resource.ResourceFileDBO resourceFile : dbo) {
			result.add(getDTO(resourceFile));
		}
		return result;
	}

	public static com.nagoya.model.to.resource.ResourceFileTO getDTO(com.nagoya.model.dbo.resource.ResourceFileDBO dbo) {
		if (dbo == null) {
			return null;
		}
		com.nagoya.model.to.resource.ResourceFileTO result = new com.nagoya.model.to.resource.ResourceFileTO();
		result.setId(dbo.getId());
		result.setName(dbo.getName());
		result.setType(dbo.getType());
		result.setContent(dbo.getContent());
		return result;
	}

}
