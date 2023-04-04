package com.luci.gamification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luci.gamification.entity.User;
import com.luci.gamification.service.UserService;



@Controller
public class MainController {

	// controller related to the main page
	
	@Autowired
	UserService userService;

	@GetMapping("/")
	public String getHome() {
		return "home";
	}

	@GetMapping("/menu")
	public String menu() {
		return "menu-page";
	}

	
	// method used for account confirmation, gets the token as a request parameter
	@GetMapping("/confirmAccount")
	public String confirmAccount(@RequestParam("token") String token, Model model) {
		
		// find the user by the token, if it is not found or it is already confirmed redirect to the login page
		User user = userService.findByConfirmationToken(token);
		if (user == null || user.getEnabled() == 1) {
			return "redirect:/login";
		}

		
		//confirm the user and set the confirmation token to null
		user.setEnabled(1);
		user.setConfirmationToken(null);
		userService.updateUser(user);

		model.addAttribute("message", "Account confirmed successfully!");
		return "account/email-confirm-page";
	}

	@GetMapping("/deleteConfirm")
	public String deleteConfirm() {
		return "account/delete-confirm-page";
	}

}
