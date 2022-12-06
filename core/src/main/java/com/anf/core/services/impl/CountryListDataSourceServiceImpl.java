package com.anf.core.services.impl;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.anf.core.services.CountryListDataSourceService;
import com.drew.lang.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/*** Begin Code - Manikandan Annamalai ***/

@Component(immediate = true, service = CountryListDataSourceService.class)
public class CountryListDataSourceServiceImpl implements CountryListDataSourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryListDataSourceServiceImpl.class);
    private static final String JSON_PATH = "/content/dam/anf-code-challenge/exercise-1/countries.json";

    private ResourceResolver resolver;
    private List<Resource> resourceList;
    @Override
    public List<Resource> getCountryListDataSource(SlingHttpServletRequest request) {
        try {
            resolver = request.getResourceResolver();
            InputStream jsonStream = getJsonStreamFromDam(resolver);

            if(jsonStream != null) {
                final List<DataSourceValueMap> data = getValueMaps(jsonStream, JSON_PATH);
                if(data != null) {
                    resourceList = data.stream().map(entry -> {
                        final ValueMap valueMap = new ValueMapDecorator(new HashMap<>());
                        valueMap.put("value", entry.getValue());
                        valueMap.put("text", entry.getText());
                        return new ValueMapResource(resolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, valueMap);
                    }).collect(Collectors.toList());

                    LOGGER.info("resourceList {}", resourceList);

                    if(resourceList != null) {
                        DataSource dataSource = new SimpleDataSource(resourceList.iterator());
                        request.setAttribute(DataSource.class.getName(), dataSource);
                    }
                }

                return resourceList;
            }


        }catch (Exception e) {
            LOGGER.error("ERROR creating data source", e);
        }

        return null;
    }

    private List<DataSourceValueMap> getValueMaps(InputStream jsonStream, String jsonPath) {
        List<DataSourceValueMap> data = new ArrayList<>();
        try {
            JsonObject object = new Gson().fromJson(new InputStreamReader(jsonStream, StandardCharsets.UTF_8), JsonObject.class);
            LOGGER.info("country {}", object.getAsJsonObject());
            Iterator<String> countryObjIterator = object.keySet().iterator();

            while (countryObjIterator.hasNext()) {
                String key = countryObjIterator.next();
                String value = object.get(key).getAsString();

                DataSourceValueMap sourceValueMap = new DataSourceValueMap();
                sourceValueMap.setValue(value);
                sourceValueMap.setText(key);

                data.add(sourceValueMap);

            }

            return  data;

        }catch (Exception e) {
            LOGGER.error("Could not get values from json ", e);
            return  null;
        }
    }

    private InputStream getJsonStreamFromDam(@NotNull ResourceResolver resolver) {
        final Session session = resolver.adaptTo(Session.class);

        try {
            Node jsonNode = JcrUtils.getNodeIfExists(JSON_PATH, session);
            if (jsonNode == null) {
                throw new IllegalArgumentException("Data source node <" + JSON_PATH + "> does not exist");
            }

            Node jcrContent = jsonNode.getNode(JcrConstants.JCR_CONTENT);
            if (jcrContent == null) {
                throw new IllegalArgumentException("Data source node <" + JSON_PATH + "> has no <" + JcrConstants.JCR_DATA + "> child");
            }

            Node rendition = jcrContent.getNode("renditions");
            Node original = rendition.getNode("original");

            Node originalNodeJcrContent = original.getNode(JcrConstants.JCR_CONTENT);

            Property jcrData = originalNodeJcrContent.getProperty(JcrConstants.JCR_DATA);
            if (jcrData == null) {
                throw new IllegalArgumentException("Node <" + jcrContent.getPath() + "> has no <" + JcrConstants.JCR_DATA + "> property");
            }

            Binary binary = jcrData.getBinary();
            if (binary == null) {
                throw new IllegalArgumentException("Property <" + JcrConstants.JCR_DATA + "> of node <" + jcrContent.getPath() + "> has no binary data");
            }

            InputStream inputStream = binary.getStream();

            LOGGER.info(inputStream.toString());
            LOGGER.info(binary.toString());
            return inputStream;


        }catch (RepositoryException e) {
            LOGGER.error("Could not read JSON", e);
            return null;
        }
    }

    public static class DataSourceValueMap {
        private String value;
        private String text;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


    /*** End Code ***/
}
