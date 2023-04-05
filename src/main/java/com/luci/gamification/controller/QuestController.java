package com.luci.gamification.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.luci.gamification.badge.GamificationBadge;
import com.luci.gamification.entity.Badge;
import com.luci.gamification.entity.Quest;
import com.luci.gamification.entity.User;
import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.quest.GamificationQuest;
import com.luci.gamification.service.BadgeService;
import com.luci.gamification.service.QuestService;
import com.luci.gamification.service.SubmissionService;
import com.luci.gamification.service.UserService;
import com.luci.gamification.user.GamificationUser;
import com.luci.gamification.utility.FileUploadUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RequestMapping("/quest")
@Controller
public class QuestController {

	// controller related to the quests and their submissions and badges
	
	@Autowired
	UserService userService;
	
	@Autowired
	QuestService questService;
	
	@Autowired
	BadgeService badgeService;
	
	@Autowired
	SubmissionService submissionService;
	
	// remove whitespace from both ends of strings
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/listQuests")
	public String listQuests(Model model) {
		List<Quest> questList = questService.findQuestsByApproval(true);
		model.addAttribute("quests", questList);
		return "quest/list-quests";
	}
	
	@GetMapping("/questDetails")
	public String questDetails(@RequestParam("questId") int questId, Model model) {
		
		// get the quest by id, passed as a request parameter
		Quest quest = questService.findQuestById(questId);
		
		model.addAttribute("quest", quest);
		
		
		return "quest/list-quest-details";
	}
	
	@GetMapping("/addQuestForm")
	public String addQuestForm(Model model, Principal principal) {
		User user = userService.findUserByUsername(principal.getName());
		
		model.addAttribute("userDetail", user.getUserDetail());
		model.addAttribute("quest", new GamificationQuest());
		
		GamificationBadge badge = new GamificationBadge();
		badge.setActivated(false);
		model.addAttribute("badge", badge);
		return "quest/add-quest";
	}
	
	
	@PostMapping("/createQuest")
	public String createQuest(@Valid @ModelAttribute("quest") GamificationQuest quest, BindingResult bindingResult,
			@Valid @ModelAttribute("badge") GamificationBadge badge, BindingResult bindingResultBadge,
			Model model, Principal principal, @RequestParam("image") MultipartFile multipartFile) throws IOException {


		// if there were errors doing validation display them
		User user = userService.findUserByUsername(principal.getName());
		UserDetail userDetail = user.getUserDetail();
		String fileName = "";
		String uploadDir = "badge-photos/";
		GamificationBadge tempBadge = new GamificationBadge();
		tempBadge.setActivated(badge.isActivated());
		if (bindingResult.hasErrors()) {
			model.addAttribute("userDetail", userDetail);
			return "quest/add-quest";
		}


		
		// check if the name is already taken, if it is display an error message
		Quest exists = questService.findQuestByName(quest.getName());
		if (exists != null) {
			model.addAttribute("quest", new GamificationQuest());
			model.addAttribute("badge", tempBadge);
			model.addAttribute("userDetail", userDetail);
			model.addAttribute("questError", "Name already taken");
			return "quest/add-quest";
		}

		Badge newBadge = null;
		
		// check if user wants to add a badge to the quest
		
		if(badge.isActivated()) {
			// check for errors
			if (bindingResultBadge.hasErrors()) {
				model.addAttribute("userDetail", userDetail);
				return "quest/add-quest";
			}
			
			// check if badge name is already used
			Badge existsBadge = badgeService.findBadgeByName(badge.getBadgeName());
			if (existsBadge != null) {
				model.addAttribute("quest", new GamificationQuest());
				model.addAttribute("badge", tempBadge);
				model.addAttribute("userDetail", userDetail);
				model.addAttribute("questError", "Badge name already taken");
				return "quest/add-quest";
			}
			
			// get the uploaded file's name
			fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			
			// check if the image is in an accepted format(jpg or png), if not display a message
			if (!(FileUploadUtil.getFileExtension(fileName).equals(".jpg")
					|| FileUploadUtil.getFileExtension(fileName).equals(".png"))) {
				model.addAttribute("quest", new GamificationQuest());
				model.addAttribute("badge", tempBadge);
				model.addAttribute("userDetail", userDetail);
				model.addAttribute("questError", "Invalid format, jpg and png allowed.");
				return "quest/add-quest";

			}

			
			// check if the file is too large (maximum 8 MBs accepted, can be modified in application properties), if it is display a message
			if (multipartFile.getSize() > 8000000) {
				model.addAttribute("quest", new GamificationQuest());
				model.addAttribute("badge", tempBadge);
				model.addAttribute("userDetail", userDetail);
				model.addAttribute("questError", "Image too big, maximum 8 MB allowed.");
				return "quest/add-quest";
			}

			
			
			newBadge = new Badge();
			quest.setTokens(quest.getTokens() + 100);
		}
		
		// check if the user has enough tokens
		

		if(userDetail.getTokens() - quest.getTokens() < 0) {
			model.addAttribute("quest", new GamificationQuest());
			model.addAttribute("badge", tempBadge);
			model.addAttribute("userDetail", userDetail);
			model.addAttribute("questError", "Not enough tokens");
			return "quest/add-quest";
		}
		
		// if everything is fine update account balance and save quest
		
		userDetail.setTokens(userDetail.getTokens() - quest.getTokens());
		Quest newQuest = new Quest();
		newQuest.setName(quest.getName());
		newQuest.setDescription(quest.getDescription());
		newQuest.setAnswer(quest.getAnswer());
		newQuest.setTokens(quest.getTokens());
		newQuest.setApproved(0);
		if(newBadge != null) {
			newBadge = badgeService.save(badge);
			String newFileName = newBadge.getId() + FileUploadUtil.getFileExtension(fileName);

			if(!FileUploadUtil.getFileExtension(fileName).equals("")) {
				newBadge.setPicture(newFileName);
				FileUploadUtil.saveFile(uploadDir, newFileName, multipartFile);
				badgeService.update(newBadge);
			}

		}
		newQuest.setBadgeId(newBadge.getId());
		newQuest.setCreatorId(user.getId());
		user.addQuest(newQuest);
		userService.updateUser(user);

		return "redirect:/quest/listQuests";

	}

	

}
