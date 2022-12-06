package com.anf.core.models;

/*** Begin Code - Manikandan Annamalai ***/


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * Country component Model Class
 */

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = CountryModel.RESOURCE_TYPE)
public class CountryModel  {

    private static final Logger LOG = LoggerFactory.getLogger(CountryModel.class);
    protected static final String RESOURCE_TYPE = "anf-code-challenge/components/form/country";
    @Inject
    private String countries;


    public String getCountries() {
        return countries;
    }
}
