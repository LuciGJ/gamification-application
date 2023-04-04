package com.luci.gamification.account;



import com.luci.gamification.validation.ValidEmail;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Email {
	
	// class used to store the email and token
	// used to change the user's email address
	
	@ValidEmail
	@NotNull(message = "Email is required")
	@Size(min = 1, message = "Email is required")
	private String email;

	private String token;
	
	// constructors

	public Email() {
	}

	public Email(String email) {
		this.email = email;
	}
	
	// getters and setters

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
