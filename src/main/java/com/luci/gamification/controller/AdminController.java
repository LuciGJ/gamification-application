package com.luci.gamification.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luci.gamification.entity.Badge;
import com.luci.gamification.entity.Quest;
import com.luci.gamification.entity.User;
import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.service.BadgeService;
import com.luci.gamification.service.QuestService;
import com.luci.gamification.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	// controller for admin options
	@Autowired
	UserService userService;

	@Autowired
	QuestService questService;

	@Autowired
	BadgeService badgeService;

	// display the account administration page with all accounts
	@GetMapping("/administrationPage")
	public String administrationPage(Model model, Principal principal) {
		model.addAttribute("users", userService.findAllUsers(principal.getName()));

		return "admin/administration-page";
	}

	@GetMapping("/submissionsPage")
	public String submissionsPage(Model model) {
		List<Quest> questList = questService.findQuestsByApproval(false);
		List<Badge> badgeList = new ArrayList<>();
		for (Quest quest : questList) {
			badgeList.add(badgeService.findBadgeById(quest.getBadgeId()));
		}
		model.addAttribute("quests", questList);
		model.addAttribute("badges", badgeList);
		return "admin/submissions-page";
	}

	// suspend an user
	@GetMapping("/suspendUser")
	public String suspendUser(@RequestParam("userId") int userId) {

		// get the user by id, which is passed as a request parameter
		User user = userService.findUserById(userId);

		// if the user is suspended, unsuspend it
		// else suspend it
		if (user.getSuspended() == 1) {
			user.setSuspended(0);
		} else {
			user.setSuspended(1);
		}

		// update the user
		userService.updateUser(user);

		return "redirect:/admin/administrationPage";
	}

	// delete the user
	@GetMapping("/deleteUser")
	public String deleteUser(@RequestParam("userId") int userId) {

		// get the user by id, passed as a request parameter, and delete it
		User user = userService.findUserById(userId);

		userService.remove(user);

		return "redirect:/admin/administrationPage";
	}

	// search the list of users
	@GetMapping("/search")
	public String search(@RequestParam("username") String username, Model model, Principal principal) {

		// get the list of users which contain the given username, without the currently
		// logged in user
		List<User> users = userService.searchUser(principal.getName(), username);

		model.addAttribute("users", users);

		return "admin/administration-page";

	}

	@GetMapping("/acceptQuest")
	public String acceptQuest(@RequestParam("questId") int questId) {

		// get the quest by id, which is passed as a request parameter
		Quest quest = questService.findQuestById(questId);

		quest.setApproved(1);

		questService.update(quest);

		return "redirect:/admin/submissionsPage";
	}

	@GetMapping("/rejectQuest")
	public String rejectQuest(@RequestParam("questId") int questId) {

		// get the quest by id, which is passed as a request parameter
		Quest quest = questService.findQuestById(questId);

		// get user detail using the quest's creator id

		UserDetail userDetail = userService.findDetailById(quest.getCreatorId());

		// refund tokens to user

		userDetail.setTokens(userDetail.getTokens() + quest.getTokens());

		Badge badge = badgeService.findBadgeById(quest.getBadgeId());

		if (badge != null) {
			badgeService.delete(badge);
		}

		questService.delete(quest);

		userService.updateUserDetail(userDetail);

		return "redirect:/admin/submissionsPage";
	}

	@GetMapping("/deleteQuest")
	public String deleteQuest(@RequestParam("questId") int questId) {

		// get the quest by id, which is passed as a request parameter
		Quest quest = questService.findQuestById(questId);

		questService.delete(quest);

		return "redirect:/quest/listQuests";
	}

}
