package com.luci.gamification.account;



import com.luci.gamification.validation.StrongPassword;
import com.luci.gamification.validation.ValidEqualFields;

import jakarta.validation.constraints.NotNull;

@ValidEqualFields.List({ @ValidEqualFields(first = "password", second = "confirmPassword") })
public class ResetPassword {

	// class used to store password, confirmation password and token
	// used to reset password
	
	@StrongPassword
	@NotNull(message = "Password is required")
	private String password;

	@NotNull(message = "Password confirmation is required")

	private String confirmPassword;

	private String token;

	// constructors
	
	public ResetPassword() {
	}

	public ResetPassword(@NotNull(message = "Password is required") String password,
			@NotNull(message = "Password confirmation is required") String confirmPassword) {
		this.password = password;
		this.confirmPassword = confirmPassword;
	}

	// getters and setters
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
