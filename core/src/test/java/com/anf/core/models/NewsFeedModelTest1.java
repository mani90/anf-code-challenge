package com.anf.core.models;

import com.anf.core.dto.Newsfeedbean;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class NewsFeedModelTest1 {

    private final AemContext aemContext = new AemContext();

    Newsfeedbean newsfeedbean;
    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(NewsFeedModel.class);
        aemContext.load().json("/com/anf/core/models/newsfeed.json", "/newsfeedcomponent");

    }

    @Test
    void getNewsfeed() {
    }

    @Test
    void getFirstNews() {
        aemContext.currentResource("/newsfeedcomponent/firstNews");
        newsfeedbean = aemContext.request().adaptTo(Newsfeedbean.class);

        assertEquals("test", newsfeedbean.getAuthor());

    }
}