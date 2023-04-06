package com.luci.gamification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	// controller to display the login page

	@GetMapping("/login")
	public String showLogin() {
		return "login-page";
	}

}
