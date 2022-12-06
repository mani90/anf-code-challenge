package com.anf.core.services.impl;

import com.anf.core.services.ResourceResolverService;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/*** Begine Code - Manikandan Annamalai ***/

@Component(
        service = ResourceResolverService.class,
        property = {
                Constants.SERVICE_ID + "= AEM Resource Resolver Service",
                Constants.SERVICE_DESCRIPTION + "= AEM Resource Resolver Service"
        }
)
public class ResourceResolverServiceImpl implements ResourceResolverService {

    private final static Logger LOG = LoggerFactory.getLogger(ResourceResolverServiceImpl.class);
    public static final String SUB_SERVICE = "interviewService";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private ResourceResolver resourceResolver;

    @Activate
    protected void  activate() {
        try{
            Map<String, Object> serviceMap = new HashMap<>();
            serviceMap.put(ResourceResolverFactory.SUBSERVICE, SUB_SERVICE);
            resourceResolver = resourceResolverFactory.getResourceResolver(serviceMap);
        }catch (Exception e) {
            LOG.error("Exception occurred while getting resource resolver: {}", e.getMessage());
        }
    }
    @Override
    public ResourceResolver getResourceResolver() {
        return  resourceResolver;
    }
}
