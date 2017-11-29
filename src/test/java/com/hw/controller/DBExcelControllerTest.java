package com.hw.controller;

import com.hw.MockMvcFactory;
import com.hw.domain.DBProperty;
import com.hw.util.JsonUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by HeWei on 2017/11/29.
 * .
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class DBExcelControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/snippets");

    @Before
    public void setUp() {
        this.mockMvc = MockMvcFactory.mockMvcWithDoc(context, restDocumentation);
    }


    @Test
    public void getMetaExcel() throws Exception {

        DBProperty params = new DBProperty();
        params.setName("postgres");
        params.setDbName("postgres");
        params.setDbType(DBProperty.DBType.postgres);
        params.setIp("localhost");
        params.setPort(5432);
        params.setUsername("postgres");
        params.setPassword("root");

        /*normal condition*/
        this.mockMvc.perform(post("/dbProperty").content(JsonUtil.toJson(params)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

}