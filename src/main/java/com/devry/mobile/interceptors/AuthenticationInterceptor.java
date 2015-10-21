package com.devry.mobile.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.devry.mobile.utils.Encrypter;

@Component
@Qualifier("authenticationInterceptor")
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private Encrypter encrypter;
	
	@Value("${client.secret}")
	private String secret;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		
		String authorization = request.getHeader("authorization");
		String dsi = request.getHeader("dsi");
		boolean authorized = true;
		
	    if (authorization == null || dsi == null || authorization.isEmpty() || dsi.isEmpty()) {
	    	authorized = false;
	    }
	    else {
	    	String hash = encrypter.encrypt(secret, dsi);
	    	System.out.println("***hash=" + hash);
	    	if (hash.compareTo(authorization) != 0) {
	    		authorized = false;
	    	}
	    }

	    if (authorized) {
	    	return true;
	    }
	    else {
	    	request.getRequestDispatcher("/unauthorized").forward(request, response);
	    	return false;	    	
	    }
	}

}
