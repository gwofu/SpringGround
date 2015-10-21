package com.devry.mobile.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
@Qualifier("loggingIntercepter")
public class LoggingInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		
		StringBuffer requestURL = request.getRequestURL();
	    String queryString = request.getQueryString();

	    if (queryString != null) {
	    	requestURL.append('?').append(queryString);
	    }

		System.out.println("Request URL:"+ requestURL.toString());
		return true;
	}
	
}
