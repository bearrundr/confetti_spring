package com.elevysi.site.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class ModalUrlAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	
	/**
	 * http://stackoverflow.com/questions/19500332/spring-security-and-json-authentication
	 */

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		response.getWriter().print("{\"responseCode\":\"SUCCESS\"}");
        response.getWriter().flush();
	}


}
