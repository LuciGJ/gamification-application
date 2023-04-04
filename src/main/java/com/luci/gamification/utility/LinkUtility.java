package com.luci.gamification.utility;

import jakarta.servlet.http.HttpServletRequest;

public class LinkUtility {
	// used to get the url
	
	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
}
