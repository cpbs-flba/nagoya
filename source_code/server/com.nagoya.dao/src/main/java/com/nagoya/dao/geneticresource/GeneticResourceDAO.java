package com.nagoya.dao.geneticresource;

import com.nagoya.dao.base.BasicDAO;
import com.nagoya.model.dbo.person.Person;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

public interface GeneticResourceDAO extends BasicDAO<com.nagoya.model.dbo.resource.GeneticResource> {

	/**
	 * Searches for the genetic resources based on the specified filter.
	 * 
	 * @param filter
	 * @param maxResults
	 * @return
	 */
	public java.util.List<com.nagoya.model.dbo.resource.GeneticResource> search(GeneticResourceFilter filter, Person owner, int maxResults);

}
