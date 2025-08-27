package com.greenWarrior.utility;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuth {

	public static String getCurrentLoggedInUser() {
		
		Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		
		if(auth==null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
			throw new AccessDeniedException("Invalid Login Credentials.");
		}
		
		return auth.getName();
	}
}
