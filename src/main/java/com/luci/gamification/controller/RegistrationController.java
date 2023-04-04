package com.luci.gamification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luci.gamification.entity.User;
import com.luci.gamification.service.EmailService;
import com.luci.gamification.service.UserService;
import com.luci.gamification.user.GamificationUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	// controller used to handle the registration
	
	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	
	// remove the whitespace from both ends of strings
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	// add a new user to the model and display the registration page
	@GetMapping("/registrationForm")
	public String registrationForm(Model theModel) {
		theModel.addAttribute("user", new GamificationUser());
		return "registration-page";
	}

	
	// process the registration form, get the input from the GamificationUser in the Model
	@PostMapping("/registrationProcess")
	public String registrationProcess(@Valid @ModelAttribute("user") GamificationUser user, BindingResult bindingResult,
			Model model, HttpServletRequest request) {

		String username = user.getUsername();

		// if there were errors doing validation display them
		
		if (bindingResult.hasErrors()) {
			return "registration-page";
		}

		
		// check if the username is already taken, if it is display a message
		User exists = userService.findUserByUsername(username);
		if (exists != null) {
			model.addAttribute("user", new GamificationUser());
			model.addAttribute("registrationError", "Username already taken");
			return "registration-page";
		}

		
		// check if the email is already taken, if it is display a message
		exists = userService.findUserByEmail(user.getEmail());

		if (exists != null) {
			model.addAttribute("user", new GamificationUser());
			model.addAttribute("registrationError", "Email already used");
			return "registration-page";
		}
		
		// if everything is fine save the user

		userService.saveUser(user, emailService, request);

		return "registration-confirmation";

	}

}
