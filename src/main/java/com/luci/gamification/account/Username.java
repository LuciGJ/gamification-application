package com.luci.gamification.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Username {
	
	// class to store username
	
	@NotNull(message = "Username is required")
	@Size(min = 1, message = "Username is required")
	private String username;

	// constructors
	
	public Username() {
	}

	public Username(String username) {
		this.username = username;
	}

	// getters and setters
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
