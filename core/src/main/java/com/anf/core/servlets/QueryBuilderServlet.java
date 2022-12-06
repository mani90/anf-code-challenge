package com.anf.core.servlets;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

/*** Begin Code - Manikandan Annamalai ***/

/**
 * This servlet uses the QueryBuilder API to fetch the results from the JCR
 */
@Component(service = Servlet.class, property = {Constants.SERVICE_DESCRIPTION + "=Query Builder servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/querybuilder"})
public class QueryBuilderServlet extends SlingSafeMethodsServlet {

    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = 2610051404257637265L;

    private static final Logger log = LoggerFactory.getLogger(QueryBuilderServlet.class);


    @Reference
    private QueryBuilder builder;


    private Session session;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        try {

            log.info("----------< Executing Query Builder Servlet >----------");

            String param = request.getParameter("param");

            log.info("Search term is: {}", param);

            ResourceResolver resourceResolver = request.getResourceResolver();

            session = resourceResolver.adaptTo(Session.class);

            Map<String, String> predicate = new HashMap<>();

            /**
             * Configuring the Map for the predicate
             */
            predicate.put("path", "/content/anf-code-challenge/us/en");
            predicate.put("type", "cq:Page");
            predicate.put("property", "jcr:content/anfCodeChallenge");
            predicate.put("property.value", "true");
            predicate.put("p.limit", "10");

            /**
             * Creating the Query instance
             */
            Query query = builder.createQuery(PredicateGroup.create(predicate), session);

            query.setStart(0);
            query.setHitsPerPage(20);

            /**
             * Getting the search results
             */
            SearchResult searchResult = query.getResult();

            for (Hit hit : searchResult.getHits()) {

                String path = hit.getPath();

                response.getWriter().println(path);
            }
        } catch (Exception e) {

            log.error(e.getMessage(), e);
        } finally {

            if (session != null) {

                session.logout();
            }
        }
    }
}

