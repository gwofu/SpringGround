package com.devry.mobile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devry.mobile.services.ServiceNowService;


@RestController
@RequestMapping("/api")
public class ServiceNowController {

    @Autowired
    private ServiceNowService serviceNowService;
               
    @RequestMapping(value="/service-now", method={RequestMethod.POST})
    public ResponseEntity<Object> createTicket(
    		@RequestParam(value="query", defaultValue="") String query,
    		@RequestParam(value="parameters", defaultValue="") String parameters) {
    	
    	return serviceNowService.createTicket(query, parameters);
    }    

    @RequestMapping(value="/service-now/location", method={RequestMethod.GET})
    public ResponseEntity<String> getLocation(
    		@RequestParam(value="limit", defaultValue="0") String limit) {
    	
    	ResponseEntity<String> o = serviceNowService.getLocation(limit);
    	
    	return o;
    }    
}