package de.neuland.jade4j.spring.template;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpringTemplateLoaderTest {
    @Test
    public void shouldCreateCorrectExtension() throws Exception {
        SpringTemplateLoader springTemplateLoader = new SpringTemplateLoader();
        springTemplateLoader.setSuffix(".jade");
        String extension = springTemplateLoader.getExtension();
        assertEquals("jade",extension);
    }
}