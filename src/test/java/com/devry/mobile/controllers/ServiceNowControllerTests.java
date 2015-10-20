package com.devry.mobile.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.devry.mobile.Application;
import com.devry.mobile.services.ServiceNowService;

import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ServiceNowControllerTests {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@InjectMocks
	ServiceNowController serviceNowController;

	@InjectMocks
    private ServiceNowService serviceNowService;
	
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
   	
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Ignore
    @Test
    public void missingQuery() throws Exception {
        this.mockMvc.perform(post("/api/service-now")
        	.param("query", "")
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isBadRequest());
    }

    @Ignore
    @Test
    public void missingCallerId() throws Exception {
        this.mockMvc.perform(post("/api/service-now")
        	.param("query", "{'short_description':'this is the short description'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isBadRequest());
    }
    
    @Ignore
    @Test
    public void badCallerId() throws Exception {
        this.mockMvc.perform(post("/api/service-now")
        	.param("query", "{'caller_id':'040373764','short_description':'this is the short description'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isBadRequest());
    }

    @Ignore
    @Test
    public void missingAttributeValue() throws Exception {
        this.mockMvc.perform(post("/api/service-now")
        	.param("query", "{'caller_id':'040373764','short_description'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isBadRequest());
    }

    @Ignore
    @Test
    public void hasParameter() throws Exception {
        this.mockMvc.perform(post("/api/service-now")
        	.param("query", "{'caller_id':'D40373764','short_description':'this is the short description'}")
        	.param("parameters", "sysparm_display_value=true")
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isCreated());
    }
    
    @Ignore
    @Test
    public void created() throws Exception {
        this.mockMvc.perform(post("/api/service-now")
        	.param("query", "{'caller_id':'D40373764','short_description':'this is the short description'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isCreated())
            .andExpect(content().contentType("application/json"));
    }

    @Ignore
    @Test
    public void createdWithMoreFields() throws Exception {
        this.mockMvc.perform(post("/api/service-now")
        	.param("query", "{'caller_id':'D40373764','short_description':'this is the short description','description':'','category':'','subcategory':'','impact':'3','urgency':'2'}")
        	.param("parameters", "")
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isCreated())
            .andExpect(content().contentType("application/json"));
    }
    
    @Test
    public void getOneLocation() throws Exception {
        this.mockMvc.perform(get("/api/service-now/location")
        	.param("limit", "1") 
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType("application/json"))
	        .andExpect(jsonPath("$.result", hasSize(1)))
	        .andExpect(jsonPath("$.result[0].name", notNullValue()));
    }

    @Test
    public void getOneHundredLocation() throws Exception {
        this.mockMvc.perform(get("/api/service-now/location")
        	.param("limit", "100") 
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType("application/json"))
	        .andExpect(jsonPath("$.result", hasSize(100)))
	        .andExpect(jsonPath("$.result[99].name", notNullValue()));
    }

    @Test
    public void getAllLocation() throws Exception {
        this.mockMvc.perform(get("/api/service-now/location")
        	.param("limit", "0") 
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType("application/json"))
	        .andExpect(jsonPath("$.result", is(not(empty()))));
    }
    
    @Test
    public void getLocation() throws Exception {
    	ResultActions actions = this.mockMvc.perform(get("/api/service-now/location")
        	.param("limit", "0") 
        	.accept(MediaType.parseMediaType("application/json;charset=UTF-8")));
    	
    	actions.andDo(MockMvcResultHandlers.print());
    	
    	MvcResult mvcResult = actions.andReturn();
    	String content = mvcResult.getResponse().getContentAsString();
    	
    	log.debug("content=" + content);
    	
    	actions
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.result", hasSize(1)))
            .andExpect(jsonPath("$.result[0].name", notNullValue()));
    }
    
}