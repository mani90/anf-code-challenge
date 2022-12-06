package com.anf.core.services.impl;

import com.anf.core.services.ContentService;
import com.day.cq.commons.jcr.JcrUtil;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;

@Component(immediate = true, service = ContentService.class)
public class ContentServiceImpl implements ContentService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);
    private final String AGE_NODE_PATH = "/etc/age";
    private final String USER_DETAILS_PATH = "/var/anf-code-challenge";

    @Override
    public boolean commitUserDetails(SlingHttpServletRequest req, ResourceResolver resourceResolver) {
        // Add your logic. Modify method signature as per need.
        /*** Begin Code - Manikandan Annamalai ***/
        int age = Integer.parseInt(req.getParameter("age"));
        boolean test = isValidAge(age, resourceResolver);
        if(isValidAge(age, resourceResolver)) {
            try {
                String firstName = req.getParameter("firstName");
                String lastName = req.getParameter("lastName");
                String country = req.getParameter("country");

                Session session = resourceResolver.adaptTo(Session.class);
                Node userDetailsNode = JcrUtil.createPath(USER_DETAILS_PATH, JcrConstants.NT_UNSTRUCTURED, session);
                userDetailsNode.setProperty("firstName", firstName);
                userDetailsNode.setProperty("lastName", lastName);
                userDetailsNode.setProperty("country", country);
                userDetailsNode.setProperty("age", age);

                session.save();

                return true;
            }catch (Exception e) {
                LOG.error("Exception while save user details in [{}] node", USER_DETAILS_PATH, e);
                return false;
            }
        }

        return false;
    }

    private boolean isValidAge(int age,  ResourceResolver resourceResolver) {
        Session session = resourceResolver.adaptTo(Session.class);

        try {
            Node ageNode = JcrUtils.getNodeIfExists(AGE_NODE_PATH, session);
            if(ageNode != null) {
                int minAge = Integer.parseInt(ageNode.getProperty("minAge").getValue().toString());
                int maxAge = Integer.parseInt(ageNode.getProperty("maxAge").getValue().toString());
                LOG.debug("MINAGE {} MAXAGE {}", maxAge, maxAge);

                if(age >= minAge && age <= maxAge) {
                    return true;
                }else {
                    return false;
                }
            }
        }catch (Exception e) {
            LOG.error("Exception while read [{}] node", AGE_NODE_PATH, e);
            return false;
        }
        return false;
    }
}

/*** End Code ***/
