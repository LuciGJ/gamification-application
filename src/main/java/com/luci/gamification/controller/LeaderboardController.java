package com.luci.gamification.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.service.UserService;

@Controller
@RequestMapping("/leaderboard")
public class LeaderboardController {

	// controller used to handle the leaderboard

	@Autowired
	UserService userService;

	// display the users based on their rank
	@GetMapping("/viewLeaderboard")

	public String viewLeaderboard(Model model, @RequestParam("page") Optional<Integer> page) {
		List<UserDetail> users = userService.getUsersByQuests();
		List<Integer> rankings = new ArrayList<>();
		// calculate rankings, in case multiple users have the same number of completed
		// quests
		int ranking = 1;
		rankings.add(ranking);
		for (int i = 1; i < users.size(); i++) {
			if (users.get(i).getQuests() < users.get(i - 1).getQuests()) {
				rankings.add(++ranking);
			} else {
				rankings.add(ranking);
			}
		}

		// get current page

		int currentPage = page.orElse(1);
		int pageSize = 10;
		currentPage--;

		// get page elements

		int startItem = currentPage * pageSize;
		List<UserDetail> usersOnPage;
		List<Integer> ranksOnPage;
		if (users.size() < startItem) {
			usersOnPage = new ArrayList<>();
			ranksOnPage = new ArrayList<>();
		} else {
			int toIndex = Math.min(startItem + pageSize, users.size());
			usersOnPage = users.subList(startItem, toIndex);
			ranksOnPage = rankings.subList(startItem, toIndex);

		}
		Page<UserDetail> userPage = new PageImpl<UserDetail>(usersOnPage, PageRequest.of(currentPage, pageSize),
				users.size());
		Page<Integer> rankPage = new PageImpl<Integer>(ranksOnPage, PageRequest.of(currentPage, pageSize),
				rankings.size());
		int totalPages = userPage.getTotalPages();

		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("users", userPage);
		model.addAttribute("rankings", rankPage);

		return "leaderboard/leaderboard";
	}

}
