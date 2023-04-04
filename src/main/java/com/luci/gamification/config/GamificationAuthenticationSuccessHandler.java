package com.luci.gamification.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component

public class GamificationAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	// redirect the user to the menu when the authentication succeeded
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		response.sendRedirect(request.getContextPath() + "/menu");

	}

}
