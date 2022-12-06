package com.anf.core.services;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import java.util.List;

/*** Begin Code - Manikandan Annamalai ***/

/**
 * Country list dropdown data source service class
 */
public interface CountryListDataSourceService {

    List<Resource>  getCountryListDataSource(SlingHttpServletRequest request);
}
