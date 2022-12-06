package com.anf.core.services;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

public interface ContentService {
	boolean commitUserDetails(SlingHttpServletRequest req, ResourceResolver resourceResolver);
}
