package com.luci.gamification.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.luci.gamification.entity.Submission;
import com.luci.gamification.entity.User;
import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.quest.GamificationQuest;
import com.luci.gamification.service.BadgeService;
import com.luci.gamification.service.QuestService;
import com.luci.gamification.service.SubmissionService;
import com.luci.gamification.service.UserService;
import com.luci.gamification.submission.GamificationSubmission;
import com.luci.gamification.utility.FileUploadUtil;

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
	public String listQuests(Model model, Principal principal, @RequestParam("page") Optional<Integer> page)
			 {

		// get all approved quests
		int currentPage = page.orElse(1);
		int pageSize = 3;
		
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		currentPage = pageable.getPageNumber();
		
//		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
//		currentPage = pageable.getPageNumber();
//		pageSize = pageable.getPageSize();
		
		List<Quest> questList = questService.findQuestsByApproval(true);

		// sort quests based on submission status
		User user = userService.findUserByUsername(principal.getName());

		List<Integer> statusList = new ArrayList<>();
		List<Quest> quests = new ArrayList<>();

		List<Quest> notSubmitted = new ArrayList<>();
		List<Quest> submitted = new ArrayList<>();
		List<Quest> accepted = new ArrayList<>();
		List<Quest> rejected = new ArrayList<>();

		int notSubmittedNumber = 0;
		int submittedNumber = 0;
		int acceptedNumber = 0;
		int rejectedNumber = 0;

		List<Submission> submissions = user.getSubmissionList();

		for (Quest quest : questList) {
			boolean added = false;
			for (Submission submission : submissions) {

				if (submission.getQuestId() == quest.getId()) {
					if (submission.getStatus() == 0) {
						submitted.add(quest);
						submittedNumber++;
					} else if (submission.getStatus() == 1) {
						accepted.add(quest);
						acceptedNumber++;
					} else {
						rejected.add(quest);
						rejectedNumber++;
					}
					added = true;
					break;
				}

			}
			if (!added) {
				notSubmittedNumber++;
				notSubmitted.add(quest);
			}
		}

		for (Quest quest : notSubmitted) {
			quests.add(quest);
		}

		for (int i = 0; i < notSubmittedNumber; i++) {
			statusList.add(3);
		}

		for (Quest quest : submitted) {
			quests.add(quest);
		}

		for (int i = 0; i < submittedNumber; i++) {
			statusList.add(0);
		}

		for (Quest quest : accepted) {
			quests.add(quest);
		}

		for (int i = 0; i < acceptedNumber; i++) {
			statusList.add(1);
		}

		for (Quest quest : rejected) {
			quests.add(quest);
		}

		for (int i = 0; i < rejectedNumber; i++) {
			statusList.add(2);
		}

		// get current page
		
		int startItem = currentPage * pageSize;
		List<Quest> questsOnPage;
		List<Integer> statusOnPage;
		
		if(quests.size() < startItem) {
			questsOnPage = new ArrayList<>();
			statusOnPage = new ArrayList<>();
		} else {
			int toIndex = Math.min(startItem + pageSize, quests.size());
            questsOnPage = quests.subList(startItem, toIndex);
            statusOnPage = statusList.subList(startItem, toIndex);
            
		}
	
		// create new object of type Page from list

		Page<Quest> questPage
        = new PageImpl<Quest>(questsOnPage, PageRequest.of(currentPage, pageSize), quests.size());
		Page<Integer> statusPage = new PageImpl<Integer>(statusOnPage, PageRequest.of(currentPage , pageSize), statusList.size());
		// get all pages

		int totalPages = questPage.getTotalPages();
		
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = 1; i <= totalPages ; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("quests", questPage);
		model.addAttribute("status", statusPage);
		return "quest/list-quests";
	}

	@GetMapping("/questDetails")
	public String questDetails(@RequestParam("questId") int questId, Model model, Principal principal) {

		User user = userService.findUserByUsername(principal.getName());
		List<Submission> submissions = user.getSubmissionList();
		// get the quest by id, passed as a request parameter
		Quest quest = questService.findQuestById(questId);

		// get the quest's badge

		Badge badge = badgeService.findBadgeById(quest.getBadgeId());

		// set submission data

		GamificationSubmission newSubmission = new GamificationSubmission();
		newSubmission.setQuestId(questId);
		newSubmission.setUserId(user.getId());
		newSubmission.setExists(false);

		// check if the user has already submitted an answer

		for (Submission submission : submissions) {
			if (submission.getQuestId() == questId) {
				newSubmission.setExists(true);
				newSubmission.setSubmissionAnswer(submission.getAnswer());
				break;
			}
		}

		model.addAttribute("quest", quest);

		model.addAttribute("badge", badge);

		model.addAttribute("submission", newSubmission);
		return "quest/list-quest-details";
	}

	@GetMapping("/listSubmissions")
	public String listSubmissions(@RequestParam("questId") int questId, Model model, Principal principal) {

		// check if the user is the creator of the quest

		Quest quest = questService.findQuestById(questId);

		User user = userService.findUserByUsername(principal.getName());

		if (user.getId() != quest.getCreatorId()) {
			return "redirect:/quest/listQuests";
		}

		// get the quest's submissions
		List<Submission> submissions = submissionService.findSubmissionsByQuest(questId);

		model.addAttribute("quest", quest);

		model.addAttribute("submissions", submissions);

		return "quest/list-submissions-page";
	}

	@GetMapping("/acceptSubmission")
	public String acceptSubmission(@RequestParam("submissionId") int submissionId, Principal principal) {

		// get the submission by id, which is passed as a request parameter

		Submission submission = submissionService.findSubmissionById(submissionId);

		// check if the current user is the creator of the quest

		Quest quest = questService.findQuestById(submission.getQuestId());

		User user = userService.findUserByUsername(principal.getName());

		if (user.getId() != quest.getCreatorId()) {
			return "redirect:/quest/listQuests";
		}

		// approve the submission

		submission.setStatus(1);

		// reward the user

		User userToReward = userService.findUserById(submission.getSubmitId());
		UserDetail userDetail = userToReward.getUserDetail();

		// if the user is the creator of the quest do not award tokens

		if (userToReward.getId() != quest.getCreatorId()) {
			userDetail.setTokens(userDetail.getTokens() + quest.getTokens());
		}

		userDetail.setQuests(userDetail.getQuests() + 1);
		// check if there is a badge and if there is award the user

		Badge badge = badgeService.findBadgeById(quest.getBadgeId());

		if (badge != null) {
			userToReward.addBadge(badge);
		}

		// save the changes

		userService.updateUser(userToReward);

		return "redirect:/quest/listSubmissions?questId=" + quest.getId();
	}

	@GetMapping("/rejectSubmission")
	public String rejectSubmission(@RequestParam("submissionId") int submissionId, Principal principal) {

		// get the submission by id, which is passed as a request parameter

		Submission submission = submissionService.findSubmissionById(submissionId);

		// check if the current user is the creator of the quest

		Quest quest = questService.findQuestById(submission.getQuestId());

		User user = userService.findUserByUsername(principal.getName());

		if (user.getId() != quest.getCreatorId()) {
			return "redirect:/quest/listQuests";
		}

		// reject the submission

		submission.setStatus(2);

		// save the changes

		submissionService.save(submission);

		return "redirect:/quest/listSubmissions?questId=" + quest.getId();
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
			@Valid @ModelAttribute("badge") GamificationBadge badge, BindingResult bindingResultBadge, Model model,
			Principal principal, @RequestParam("image") MultipartFile multipartFile) throws IOException {

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

		if (badge.isActivated()) {
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

			// check if the image is in an accepted format(jpg or png), if not display a
			// message
			if (!(FileUploadUtil.getFileExtension(fileName).equals(".jpg")
					|| FileUploadUtil.getFileExtension(fileName).equals(".png"))) {
				model.addAttribute("quest", new GamificationQuest());
				model.addAttribute("badge", tempBadge);
				model.addAttribute("userDetail", userDetail);
				model.addAttribute("questError", "Invalid format, jpg and png allowed.");
				return "quest/add-quest";

			}

			// check if the file is too large (maximum 8 MBs accepted, can be modified in
			// application properties), if it is display a message
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

		if (userDetail.getTokens() - quest.getTokens() < 0) {
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
		if (newBadge != null) {
			newBadge = badgeService.save(badge);
			String newFileName = newBadge.getId() + FileUploadUtil.getFileExtension(fileName);

			if (!FileUploadUtil.getFileExtension(fileName).equals("")) {
				newBadge.setPicture(newFileName);
				FileUploadUtil.saveFile(uploadDir, newFileName, multipartFile);
				badgeService.update(newBadge);
			}

		}
		if (newBadge != null) {
			newQuest.setBadgeId(newBadge.getId());
		}
		newQuest.setCreatorId(user.getId());
		user.addQuest(newQuest);
		userService.updateUser(user);
		model.addAttribute("message", "The quest has been successfully created and is now awating approval.");

		return "quest/quest-confirm-page";

	}

	@PostMapping("/submissionProcess")
	public String submissionProcess(@Valid @ModelAttribute("submission") GamificationSubmission submission,
			BindingResult bindingResult, Model model) {

		// if there were errors doing validation display them

		GamificationSubmission tempSubmission = new GamificationSubmission();
		tempSubmission.setExists(submission.isExists());
		tempSubmission.setQuestId(submission.getQuestId());
		tempSubmission.setUserId(submission.getUserId());
		User user = userService.findUserById(submission.getUserId());

		if (bindingResult.hasErrors()) {
			model.addAttribute("submission", tempSubmission);
			model.addAttribute("userDetail", user.getUserDetail());
			return "quest/add-quest";
		}

		// check if the user already submitted

		for (Submission sub : user.getSubmissionList()) {
			if (sub.getQuestId() == submission.getQuestId()) {
				return "redirect:/quest/listQuests";
			}
		}

		// if everything is fine send the submission

		Submission newSubmission = new Submission();
		newSubmission.setAnswer(submission.getSubmissionAnswer());
		newSubmission.setQuestId(submission.getQuestId());
		newSubmission.setStatus(0);
		newSubmission.setSubmitId(submission.getUserId());
		user.addSubmission(newSubmission);
		userService.updateUser(user);
		model.addAttribute("message", "The submission has been successfully sent and is now awating approval.");

		return "quest/quest-confirm-page";

	}

	@GetMapping("/userSubmissions")
	public String userSubmissions(Model model, Principal principal) {
		User user = userService.findUserByUsername(principal.getName());

		List<Submission> submissions = user.getSubmissionList();
		List<Quest> quests = new ArrayList<>();

		// populate the list with the quests from user's submissions

		for (Submission submission : submissions) {
			Quest quest = questService.findQuestById(submission.getQuestId());

			// the quest might be null since it can be removed

			if (quest == null) {
				quest = new Quest();
				quest.setId(-1);
			}
			quests.add(quest);
		}

		model.addAttribute("quests", quests);
		if (submissions == null) {
			model.addAttribute("submissions", new ArrayList<Submission>());
		} else {
			model.addAttribute("submissions", submissions);
		}

		return "quest/list-user-submissions";
	}

	@GetMapping("/userQuests")
	public String userQuest(Model model, Principal principal) {
		User user = userService.findUserByUsername(principal.getName());

		List<Quest> quests = user.getQuestList();
		List<Badge> badges = new ArrayList<>();

		// populate the list with the badges from user's quests

		for (Quest quest : quests) {
			badges.add(badgeService.findBadgeById(quest.getBadgeId()));
		}

		model.addAttribute("quests", quests);
		model.addAttribute("badges", badges);

		return "quest/list-user-quests";
	}

}
