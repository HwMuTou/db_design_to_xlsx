package com.hw;

import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

/**
 * Created by HeWei on 2017/8/14.
 * .
 */
public class MockMvcFactory {

    public static MockMvc mockMvcWithDoc(WebApplicationContext context, JUnitRestDocumentation restDocumentation) {
        return MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{class-name}/{method-name}/{step}/")).build();
    }

    public static MockMvc mockMvcWithoutDoc(WebApplicationContext context, JUnitRestDocumentation restDocumentation) {
        return MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }
}
