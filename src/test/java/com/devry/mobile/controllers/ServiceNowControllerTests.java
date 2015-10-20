package com.devry.mobile.controllers;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.devry.mobile.Application;
import com.devry.mobile.services.ServiceNowService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ServiceNowControllerTests {

	@InjectMocks
	ServiceNowController serviceNowController;

	@InjectMocks
    private ServiceNowService serviceNowService;
	
    private MockMvc mockMvc;
    private static final String INCIDENT_PATH     = "/api/service-now";
    private static final String LOCATION_PATH     = "/api/service-now/locations";
    private static final String CATEGORY_PATH     = "/api/service-now/categories";
    private static final String SUB_CATEGORY_PATH = "/api/service-now/sub-categories";
    private static final String JSON_TYPE_UTF_8   = "application/json;charset=UTF-8";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
   	
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void missingQuery() throws Exception {
        this.mockMvc.perform(post(INCIDENT_PATH)
        	.param("query", "")
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void missingCallerId() throws Exception {
        this.mockMvc.perform(post(INCIDENT_PATH)
        	.param("query", "{'short_description':'this is the short description'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void badCallerId() throws Exception {
        this.mockMvc.perform(post(INCIDENT_PATH)
        	.param("query", "{'caller_id':'040373764','short_description':'this is the short description'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void missingAttributeValue() throws Exception {
        this.mockMvc.perform(post(INCIDENT_PATH)
        	.param("query", "{'caller_id':'040373764','short_description'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void hasParameter() throws Exception {
        this.mockMvc.perform(post(INCIDENT_PATH)
        	.param("query", "{'caller_id':'D40373764','short_description':'this is the short description'}")
        	.param("parameters", "sysparm_display_value=true")
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
            .andExpect(status().isCreated());
    }
    
    @Test
    public void created() throws Exception {
        this.mockMvc.perform(post(INCIDENT_PATH)
        	.param("query", "{'caller_id':'D40373764','short_description':'this is the short description'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void createdWithMoreFields() throws Exception {
        this.mockMvc.perform(post(INCIDENT_PATH)
        	.param("query", "{'caller_id':'D40373764','short_description':'this is the short description','description':'','category':'','subcategory':'','impact':'3','urgency':'2'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void getOneLocation() throws Exception {
        this.mockMvc.perform(get(LOCATION_PATH)
        	.param("limit", "1") 
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.result", hasSize(1)))
	        .andExpect(jsonPath("$.result[0].name", notNullValue()));
    }

    @Test
    public void getOneHundredLocation() throws Exception {
        this.mockMvc.perform(get(LOCATION_PATH)
        	.param("limit", "100") 
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.result", hasSize(100)))
	        .andExpect(jsonPath("$.result[99].name", notNullValue()));
    }

    @Test
    public void getAllLocation() throws Exception {
        this.mockMvc.perform(get(LOCATION_PATH)
        	.param("limit", "0") 
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.result", is(not(empty()))));
    }

    @Test
    public void getCategories() throws Exception {
        this.mockMvc.perform(get(CATEGORY_PATH)
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.result", is(not(empty()))));
    }

    @Test
    public void getSubCategories() throws Exception {
        this.mockMvc.perform(get(SUB_CATEGORY_PATH)
        	.accept(MediaType.parseMediaType(JSON_TYPE_UTF_8)))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.result", is(not(empty()))));
    }
}