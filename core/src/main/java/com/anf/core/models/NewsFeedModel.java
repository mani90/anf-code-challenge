package com.anf.core.models;

/*** Begin Code - Manikandan Annamalai ***/

import com.anf.core.dto.Newsfeedbean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * News Feed Model Class
 */
@Model(adaptables = Resource.class)
public class NewsFeedModel {

    protected final Logger LOG = LoggerFactory.getLogger(NewsFeedModel.class);
    private final String NEWS_FEED_CONTENT = "/var/commerce/products/anf-code-challenge/newsData";

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource currentResource;

    private List<Newsfeedbean> newsfeed;

    private Newsfeedbean firstNews;

    private String ts;

    @PostConstruct
    protected void init(){
        try {
            newsfeed = new ArrayList<>();
            LOG.debug("init {}", NEWS_FEED_CONTENT);
            Session session = resourceResolver.adaptTo(Session.class);
            if (session.nodeExists(NEWS_FEED_CONTENT)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY");
                Date currentDate = new Date();
                String cDate = simpleDateFormat.format(currentDate);
                Node parentNode = session.getNode(NEWS_FEED_CONTENT);
                int index = 0;
                if(parentNode.hasNodes()) {
                    NodeIterator childNode =  parentNode.getNodes();
                    while (childNode.hasNext()){
                        Node newsFeedNode = childNode.nextNode();
                        if (newsFeedNode.hasProperties()) {
                            PropertyIterator iterator = newsFeedNode.getProperties();
                            JsonObject obj = new JsonObject();
                            while (iterator.hasNext()) {
                                Property property = iterator.nextProperty();
                                if(property.getName() != "jcr:primaryType") {
                                    obj.addProperty(property.getName(), property.getValue().toString());
                                }
                            }

                            obj.addProperty("currentDate", cDate);

                            Newsfeedbean newsfeedbean = new Gson().fromJson(obj, Newsfeedbean.class);
                            if(index != 0)
                                newsfeed.add(newsfeedbean);
                            else {
                                firstNews = newsfeedbean;
                                LOG.info("first {}", obj);
                            }
                        }

                        index++;
                    }

                }
            }
        }catch (Exception e) {
            LOG.error("Exception while reading news feed node {}", e.getMessage());
        }
    }

    public List<Newsfeedbean> getNewsfeed() {
        return newsfeed;
    }

    public Newsfeedbean getFirstNews() {
        return firstNews;
    }

    public String getTs() {
        return "mani";
    }

    /*** End code ***/
}
