package com.anf.core.servlets;

/*** Begin Code - Manikandan Annamalai ***/

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.anf.core.services.CountryListDataSourceService;
import com.anf.core.services.impl.CountryListDataSourceServiceImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.util.List;

/**
 * Country list data source servlet
 */
@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "= Country list datasource servlet",
                "sling.servlet.paths =/bin/countryList",
                "sling.servlet.methods = " + HttpConstants.METHOD_GET
        }
)
public class CountryListServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryListServlet.class);
    private static final String JSON_PATH = "/content/dam/anf-code-challenge/exercise-1/countries.json";

    private ResourceResolver resolver;
    private List<Resource> resourceList;

    @Reference
    private CountryListDataSourceService countryListDataSourceService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            resourceList = countryListDataSourceService.getCountryListDataSource(request);
            if(resourceList != null) {
                LOGGER.debug("resourceList {}", resourceList);

                if(resourceList != null) {
                    DataSource dataSource = new SimpleDataSource(resourceList.iterator());
                    request.setAttribute(DataSource.class.getName(), dataSource);
                }
            }

        }catch (Exception e) {
            LOGGER.error("ERROR creating data source", e);
        }

    }

    /*** End Code ***/
}
