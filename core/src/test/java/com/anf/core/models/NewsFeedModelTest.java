package com.anf.core.models;

/*** Begin Code - Manikandan Annamalai ***/

import com.anf.core.dto.Newsfeedbean;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Newsfeed component model test class
 */
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class NewsFeedModelTest {


    private final AemContext aemContext = new AemContext();

    Newsfeedbean newsfeedbean;
    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(NewsFeedModel.class);
        aemContext.load().json("/com/anf/core/models/newsfeed.json", "/content/newsfeedcomponent");

    }

    @Test
    void getNewsfeed() {
    }

    @Test
    void getFirstNews() {
        aemContext.currentResource("/content/newsfeedcomponent");
        newsfeedbean = aemContext.request().adaptTo(Newsfeedbean.class);
        System.out.println("ctx "+aemContext.request().adaptTo(Newsfeedbean.class));
        //assertEquals("test", newsfeedbean);

    }
}
