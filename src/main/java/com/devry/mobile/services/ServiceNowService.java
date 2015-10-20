package com.devry.mobile.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.devry.mobile.utils.ServiceNowUtils;

/**
 * ServiceNowService provides services to call ServiceNow REST APIs.
 * 
 * Default ServiceNow incident URL: 
 *   https://devryeducationgroupdev.service-now.com/api/now/table/incident?sysparm_display_value=all&sysparm_exclude_reference_link=false&sysparm_input_display_value=false&sysparm_suppress_auto_sys_field=false
 *
 * Default ServiceNow location URL: 
 *   https://devryeducationgroupdev.service-now.com/api/now/table/cmn_location?sysparm_limit=10&sysparm_query=u_active=true&sysparm_display_value=true&sysparm_exclude_reference_link=false&sysparm_fields=name
 * 
 * Default ServiceNow category URL:
 *   https://devryeducationgroupdev.service-now.com/api/now/table/sys_choice?sysparm_limit=50&sysparm_query=nameLIKEinc^elementSTARTSWITHcategory^inactive=false&sysparm_display_value=true&sysparm_exclude_reference_link=&sysparm_fields=label,sequence
 *
 * REGULAR_EXPRESSION is the pattern to check required caller_id and short_description fields exist.
 * 
 * @author Gwowen Fu
 *
 */
@Component
public class ServiceNowService {

    private static final String INCEDENT_PATH	       = "/incident?";
    private static final String INCEDENT_PARAMETER     = "sysparm_display_value=all&sysparm_exclude_reference_link=false&sysparm_input_display_value=false&sysparm_suppress_auto_sys_field=false";
    private static final String REGULAR_EXPRESSION     = "^\\{(?=.*'caller_id':'\\D\\d{8}')(?=.*'short_description':'.+').*\\}$";
    private static final String LOCATION_PATH	       = "/cmn_location?";
    private static final String LOCATION_PARAMETER     = "sysparm_limit=??&sysparm_query=u_active=true&sysparm_fields=name";
    private static final String CATEGORY_PATH	       = "/sys_choice?";
    private static final String CATEGORY_PARAMETER 	   = "sysparm_query=nameLIKEinc^elementSTARTSWITHcategory^inactive=false&sysparm_display_value=true&sysparm_exclude_reference_link=&sysparm_fields=label,sequence";
    private static final String SUB_CATEGORY_PARAMETER = "sysparm_query=name=incident^element=subcategory^inactive=false&sysparm_display_value=true&sysparm_fields=label,sequence,dependent_value";

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${servicenow.url}")
    private String SERVICE_NOW_URL;

	private HttpHeaders httpHeaders;
	
	@Autowired
	private void setHttpHeaders(ServiceNowUtils serviceNowUtils) {
		httpHeaders = serviceNowUtils.getHttpHeaders();
	}

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
		String url = parameters.isEmpty() ? (SERVICE_NOW_URL + INCEDENT_PATH + INCEDENT_PARAMETER) : (SERVICE_NOW_URL + INCEDENT_PATH + parameters);
		log.debug("ServiceNow URL=" + url);		
		HttpEntity<String> entity = new HttpEntity<String>(query, httpHeaders);        
        return restTemplate.postForEntity(url, entity, Object.class);			
	}
	
	/**
	 * Get Institute locations.
	 * 
	 * @param limit the maximum number of locations to return. When limit is set to "0" then all locations are returned. 
	 * @return      the ResponseEntity<Object> that contains the response body and status code
	 */
	public ResponseEntity<Object> getLocations(final String limit) {
        RestTemplate restTemplate = new RestTemplate();        
		String url = SERVICE_NOW_URL + LOCATION_PATH + LOCATION_PARAMETER.replace("??", limit);	
		log.debug("ServiceNow URL=" + url);
		HttpEntity<Object> entity = new HttpEntity<Object>("", httpHeaders);
		return restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);		
	}

	/**
	 * Get Incident categories.
	 * 
	 * @return ResponseEntity<Object> that contains the response body and status code
	 */
	public ResponseEntity<Object> getCategories() {
        RestTemplate restTemplate = new RestTemplate();        
		String url = SERVICE_NOW_URL + CATEGORY_PATH + CATEGORY_PARAMETER;
		log.debug("ServiceNow URL=" + url);
		HttpEntity<Object> entity = new HttpEntity<Object>("", httpHeaders);
		return restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);				
	}
	
	/**
	 * Get Incident sub-categories.
	 * 
	 * @return ResponseEntity<Object> that contains the response body and status code
	 */
	public ResponseEntity<Object> getSubCategories() {
        RestTemplate restTemplate = new RestTemplate();
		String url = SERVICE_NOW_URL + CATEGORY_PATH + SUB_CATEGORY_PARAMETER;
		log.debug("ServiceNow URL=" + url);
		HttpEntity<Object> entity = new HttpEntity<Object>("", httpHeaders);
		return restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);		
	}	
}
