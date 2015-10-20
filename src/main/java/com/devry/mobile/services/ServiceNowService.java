package com.devry.mobile.services;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * ServiceNowService provides services to call ServiceNow REST APIs.
 * 
 * The default ServiceNow incident URL: 
 *   https://devryeducationgroupdev.service-now.com/api/now/table/incident?sysparm_display_value=all&sysparm_exclude_reference_link=false&sysparm_input_display_value=false&sysparm_suppress_auto_sys_field=false
 *
 * The default ServiceNow location URL: 
 *   https://devryeducationgroupdev.service-now.com/api/now/table/cmn_location?sysparm_limit=10&sysparm_query=u_active=true&sysparm_display_value=true&sysparm_exclude_reference_link=false&sysparm_fields=name
 *   
 * @author Gwowen Fu
 *
 */
@Component
public class ServiceNowService {

	@Value("${servicenow.url}")
    private String SERVICE_NOW_URL;

	@Value("${servicenow.authorization.code}")
    private String AUTHORIZATION_CODE;
	
    private static final String INCEDENT_PARAMETER = "sysparm_display_value=all&sysparm_exclude_reference_link=false&sysparm_input_display_value=false&sysparm_suppress_auto_sys_field=false";
    private static final String REGULAR_EXPRESSION = "^\\{(?=.*'caller_id':'\\D\\d{8}')(?=.*'short_description':'.+').*\\}$";
    private static final String INCEDENT_PATH	   = "/incident?";
    private static final String LOCATION_PARAMETER = "sysparm_limit=??&sysparm_query=u_active=true&sysparm_fields=name";
    private static final String LOCATION_PATH	   = "/cmn_location?";

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public ServiceNowService() {}
	
	/**
	 * Create a ServiceNow ticket. 
	 * 
	 * @param query the simplest query string format: {'caller_id':'D40373764','short_description':'this is the short description','description':'','category':'','subcategory':'','impact':'4',"urgency":"3"}
	 * @param parameters
	 * @return      the ResponseEntity<Object> that contains the response body and status code
	 */
	public ResponseEntity<Object> createTicket(final String query, final String parameters) {
    	log.debug("createTicket() query=" + query);

		if (!query.matches(REGULAR_EXPRESSION)) {
	        return new ResponseEntity<Object>(query, null, HttpStatus.BAD_REQUEST);
		}
				
        RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", AUTHORIZATION_CODE);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(query, headers);
        
		String url = parameters.isEmpty() ? (SERVICE_NOW_URL + INCEDENT_PATH + INCEDENT_PARAMETER) : (SERVICE_NOW_URL + parameters);

		log.debug("ServiceNow URL=" + url);
		
        return restTemplate.postForEntity(url, entity, Object.class);			
	}
	
	/**
	 * Get Institute locations.
	 * 
	 * @param limit the maximum number of locations to return. When limit is set to "0" then all locations are returned. 
	 * @return      the ResponseEntity<Object> that contains the response body and status code
	 */
	public ResponseEntity<String> getLocation(final String limit) {
    	log.debug("getLocation() limit=" + limit);

        RestTemplate restTemplate = new RestTemplate();
        
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", AUTHORIZATION_CODE);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
        
		String url = SERVICE_NOW_URL + LOCATION_PATH + LOCATION_PARAMETER.replace("??", limit);
		log.debug("ServiceNow URL=" + url);
		
		HttpEntity<String> entity = new HttpEntity<String>("", headers);

		ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);		
		
		log.debug("result body=" + result.getBody());
		
        return result;	
	}
}
