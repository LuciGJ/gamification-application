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
import org.springframework.web.bind.annotation.RequestParam;

import com.luci.gamification.account.Email;
import com.luci.gamification.account.EmailDetails;
import com.luci.gamification.account.ResetPassword;
import com.luci.gamification.account.Username;
import com.luci.gamification.entity.User;
import com.luci.gamification.service.EmailService;
import com.luci.gamification.service.UserService;
import com.luci.gamification.utility.LinkUtility;
import com.luci.gamification.utility.RandomStringBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RequestMapping("/recovery")
@Controller
public class DataRecoveryController {

	// controller used to handle data recovery
	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	// remove whitespaces from both ends of strings
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/forgotConfirmation")
	public String forgotConfirmation() {
		return "recovery/forgot-confirmation";
	}

	
	// get the email from user
	@GetMapping("/forgotUsername")
	public String forgotUsername(Model model) {
		model.addAttribute("email", new Email());
		return "recovery/forgot-username";
	}

	
	// if the email is valid and an account is registered with it, send the username
	@PostMapping("/forgotUsernameProcess")
	public String forgotUsernameProcess(@Valid @ModelAttribute("email") Email email, BindingResult bindingResult,
			Model model) {

		
		// validate the email
		if (bindingResult.hasErrors()) {
			return "recovery/forgot-username";
		}

		
		// check if it exists
		User exists = userService.findUserByEmail(email.getEmail());
		if (exists == null) {
			model.addAttribute("email", new Email());
			model.addAttribute("emailError", "The email does not belong to any account");
			return "recovery/forgot-username";
		}

		
		// if everything is fine send an email containing the username
		EmailDetails details = new EmailDetails();

		details.setRecipient(email.getEmail());
		details.setMsgBody("Your username is: " + exists.getUsername());
		details.setSubject("Gamification username");
		model.addAttribute("message", "An email containing your username has been sent to you.");
		emailService.sendEmail(details);
		return "recovery/confirm-page";

	}

	// get the username from user
	@GetMapping("/forgotPassword")
	public String showForgotPasswordForm(Model model) {
		model.addAttribute("username", new Username());
		return "recovery/forgot-password";
	}

	
	// if the username is correct and exists, send a password reset link
	@PostMapping("/forgotPasswordProcess")
	public String forgotPasswordProcess(@Valid @ModelAttribute("username") Username username,
			BindingResult bindingResult, Model model, HttpServletRequest request) {

		// check if it is valid
		if (bindingResult.hasErrors()) {
			return "recovery/forgot-password";
		}

		// check if it exists
		User exists = userService.findUserByUsername(username.getUsername());
		if (exists == null) {
			model.addAttribute("username", new Username());
			model.addAttribute("usernameError", "The username does not belong to any account");
			return "recovery/forgot-password";
		}

		// create a new password recovery token
		String token;
		do {
			token = RandomStringBuilder.buildRandomString(30);
		} while (userService.findByResetPasswordToken(token) != null);
		
		// set the token, create the link and send it to the user's email
		userService.updateResetPasswordToken(token, exists.getEmail());
		String resetPasswordLink = LinkUtility.getSiteURL(request) + "/recovery/resetPassword?token=" + token;
		EmailDetails details = new EmailDetails();

		details.setRecipient(exists.getEmail());
		details.setMsgBody("Click the following link to reset your password: \n " + resetPasswordLink);
		details.setSubject("Gamification password recovery");
		model.addAttribute("message", "An email containing a link to reset your password has been sent to you.");
		emailService.sendEmail(details);
		return "recovery/confirm-page";

	}

	// reset password
	@GetMapping("/resetPassword")
	public String showResetPasswordForm(@RequestParam("token") String token, HttpServletRequest request, Model model) {
		// check if the token exists
		User user = userService.findByResetPasswordToken(token);
		ResetPassword resetPassword = new ResetPassword();
		resetPassword.setToken(token);
		model.addAttribute("resetPassword", resetPassword);

		if (user == null) {
			return "redirect:/";
		}

		return "recovery/reset-password-form";
	}

	// process reset password form
	@PostMapping("/resetPasswordProcess")
	public String resetPasswordProcess(@Valid @ModelAttribute("resetPassword") ResetPassword resetPassword,
			BindingResult bindingResult, Model model) {

		// check if the password is valid
		if (bindingResult.hasErrors()) {
			return "recovery/reset-password-form";
		}

		// get the user by the reset password token and update his password
		String password = resetPassword.getPassword();
		User exists = userService.findByResetPasswordToken(resetPassword.getToken());
		if (exists == null) {
			return "redirect:/";
		}

		userService.updatePassword(exists.getUsername(), password);
		return "recovery/reset-confirmation";

	}

}
