package com.luci.gamification.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.luci.gamification.entity.User;
import com.luci.gamification.service.EmailService;
import com.luci.gamification.service.UserService;
import com.luci.gamification.utility.LinkUtility;
import com.luci.gamification.utility.RandomStringBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RequestMapping("/account")
@Controller
public class AccountManagementController {
	// controller for account management
	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	// remove whitespace before and after string
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	// display options page

	@GetMapping("/listOptions")
	public String listOptions() {
		return "account/account-options-page";
	}

	// change email form

	@GetMapping("/changeEmail")
	public String changeEmailPage(Model model) {

		model.addAttribute("email", new Email());
		return "account/change-email-page";
	}

	// process change email form

	@PostMapping("/changeEmailProcess")
	public String changeEmailProcess(@Valid @ModelAttribute("email") Email email, BindingResult bindingResult,
			Model model, Principal principal, HttpServletRequest request) {

		// check if email is valid

		if (bindingResult.hasErrors()) {
			return "account/change-email-page";
		}

		// check if user has given his correct email address
		User user = userService.findUserByUsername(principal.getName());
		if (!email.getEmail().equals(user.getEmail())) {
			model.addAttribute("email", new Email());
			model.addAttribute("emailError", "The email address is wrong");
			return "account/change-email-page";
		}

		// create an email change token, generate an email change link and send it to
		// the user
		String token;
		do {
			token = RandomStringBuilder.buildRandomString(30);
		} while (userService.findByChangeEmailToken(token) != null);
		userService.updateChangeEmailToken(token, user.getEmail());
		String changeEmailLink = LinkUtility.getSiteURL(request) + "/account/changeEmailForm?token=" + token;
		EmailDetails details = new EmailDetails();

		details.setRecipient(user.getEmail());
		details.setMsgBody("Click the following link to change your email address: \n " + changeEmailLink);
		details.setSubject("CV Generator change email");

		emailService.sendEmail(details);
		model.addAttribute("message", "An email has been sent to confirm your identity.");
		return "account/confirm-management-page";

	}

	// form to change the email
	@GetMapping("/changeEmailForm")
	public String showNewEmail(@RequestParam("token") String token, HttpServletRequest request, Model model) {
		// get user by email change token
		User user = userService.findByChangeEmailToken(token);
		Email email = new Email();
		email.setToken(token);
		model.addAttribute("email", email);

		// if user is null then there is no active token, redirect to the main page
		if (user == null) {
			return "redirect:/";
		}

		return "account/new-email-page";
	}

	// process new email form
	@PostMapping("/newEmailProcess")
	public String newEmailProcess(@Valid @ModelAttribute("email") Email email, BindingResult bindingResult, Model model,
			Principal principal) {

		// check if the new email is valid
		if (bindingResult.hasErrors()) {
			return "account/new-email-page";
		}

		// check if the email is already taken
		if (userService.findUserByEmail(email.getEmail()) != null) {
			model.addAttribute("emailError", "The email already belongs to an account");
			return "account/new-email-page";
		}

		// check if the user is logged in
		User user = userService.findUserByUsername(principal.getName());

		if (user == null) {
			return "redirect:/login";
		}

		// check if the token is valid
		if (!user.getEmailToken().equals(email.getToken())) {
			model.addAttribute("message", "Invalid token");
			return "account/confirm-management-page";
		}

		// set the new email
		user.setEmail(email.getEmail());
		user.setEmailToken(null);
		userService.updateUser(user);
		model.addAttribute("message", "Email changed successfully!");
		return "account/confirm-management-page";

	}

	// change password page
	@GetMapping("/changePassword")
	public String changePassword(Model model) {
		model.addAttribute("resetPassword", new ResetPassword());
		return "account/change-password-form";
	}

	// process change password form
	@PostMapping("/changePasswordProcess")
	public String changePasswordProcess(@Valid @ModelAttribute("resetPassword") ResetPassword resetPassword,
			BindingResult bindingResult, Model model, Principal principal, BCryptPasswordEncoder passwordEncoder) {

		// check if the password is valid
		if (bindingResult.hasErrors()) {
			return "account/change-password-form";
		}

		// check if the user is logged in
		User user = userService.findUserByUsername(principal.getName());

		if (user == null) {
			return "redirect:/login";
		}

		// encode the password and set it
		user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
		userService.updateUser(user);
		model.addAttribute("message", "Password changed successfully!");
		return "account/confirm-management-page";

	}

	// delete account page
	@GetMapping("/deleteAccount")
	public String deleteAccount(Model model) {
		model.addAttribute("email", new Email());
		return "account/delete-account-page";
	}

	// process delete account form
	@PostMapping("/deleteAccountProcess")
	public String deleteAccountProcess(@Valid @ModelAttribute("email") Email email, BindingResult bindingResult,
			Model model, Principal principal, HttpServletRequest request) {

		// check if the email is valid
		if (bindingResult.hasErrors()) {
			return "account/delete-account-page";
		}

		// check if the user is logged in
		User user = userService.findUserByUsername(principal.getName());

		if (user == null) {
			return "redirect:/login";
		}

		// check if the user has entered his corect email
		if (!user.getEmail().equals(email.getEmail())) {
			model.addAttribute("emailError", "The email address is wrong.");
			return "account/delete-account-page";
		}

		// remove user
		userService.remove(user);

		// logout the user
		try {
			request.logout();
		} catch (ServletException e) {
			e.printStackTrace();
		}

		return "redirect:/deleteConfirm";

	}

}
