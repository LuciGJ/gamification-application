package com.luci.gamification.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.luci.gamification.entity.User;
import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.service.UserService;
import com.luci.gamification.utility.FileUploadUtil;


@Controller
@RequestMapping("/userdata")
public class UserDetailController {

	// controller to handle the user's profile
	
	@Autowired
	UserService userService;

	
	// remove whitespace from both ends of strings
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	
	
	// display user's profile
	@GetMapping("/listDetail")
	public String showDetails(Principal principal, Model model) {

		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("userDetail", user.getUserDetail());

		return "userdetails/list-user-details";
	}

	// form for modifying the user's profile
	@GetMapping("/updateDetailForm")
	public String updateDetailForm(Principal principal, Model model) {

		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("userDetail", user.getUserDetail());
		return "userdetails/modify-details";
	}

	
	// process user profile updates
	@PostMapping("/updateDetail")
	public String updateDetail(Principal principal, @ModelAttribute("userDetail") UserDetail userDetail, Model model) {

		User user = userService.findUserByUsername(principal.getName());
		
		if(userDetail.getDisplayName() == null) {
			model.addAttribute("message", "The display name cannot be null");
			return "userdetails/modify-details";
		}
		
		UserDetail exists = userService.findDetailByDisplayName(userDetail.getDisplayName());
		
		if(exists != null && !userDetail.getDisplayName().equals(user.getUserDetail().getDisplayName())) {
			model.addAttribute("message", "Display name already taken");
			return "userdetails/modify-details";
		}
		
		user.setUserDetail(userDetail);

		userService.updateUser(user);

		return "redirect:/userdata/listDetail";
	}

	
	// display a page where the user can upload a profile picture
	@GetMapping("/uploadPictureForm")
	public String uploadPictureForm(Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("userDetail", user.getUserDetail());

		return "userdetails/user-photo";
	}

	
	// handle picture upload
	@PostMapping("/uploadPicture")
	public String uploadPicture(Model model, Principal principal, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {

		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("userDetail", user.getUserDetail());

		
		// get the uploaded file's name
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		
		// check if the image is in an accepted format(jpg or png), if not display a message
		if (!(FileUploadUtil.getFileExtension(fileName).equals(".jpg")
				|| FileUploadUtil.getFileExtension(fileName).equals(".png"))) {
			model.addAttribute("imageError", "Invalid format, jpg and png allowed.");
			return "userdetails/user-photo";

		}

		
		// check if the file is too large (maximum 8 MBs accepted, can be modified in application properties), if it is display a message
		if (multipartFile.getSize() > 8000000) {
			model.addAttribute("imageError", "Image too big, maximum 8 MB allowed.");
			return "userdetails/user-photo";
		}

		String uploadDir = "user-photos/";

		
		// delete the user's old profile picture if he had one
		File deleteFile;

		if (FileUploadUtil.getFileExtension(fileName).equals(".png")) {
			deleteFile = new File(uploadDir + user.getId() + ".jpg");
			Files.deleteIfExists(deleteFile.toPath());
		} else {
			deleteFile = new File(uploadDir + user.getId() + ".png");
			Files.deleteIfExists(deleteFile.toPath());
		}

		
		// name the file as user's id + extension
		String newFileName = user.getId() + FileUploadUtil.getFileExtension(fileName);

		user.getUserDetail().setImage(newFileName);

		userService.updateUser(user);

		FileUploadUtil.saveFile(uploadDir, newFileName, multipartFile);

		return "redirect:/userdata/uploadPictureForm";
	}

}
