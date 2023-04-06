package com.luci.gamification.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.service.UserService;

@Controller
@RequestMapping("/leaderboard")
public class LeaderboardController {

	@Autowired
	UserService userService;
	
	
	@GetMapping("/viewLeaderboard")
	public String viewLeaderboard(Model model) {
		List<UserDetail> users = userService.getUsersByQuests();
		List<Integer> rankings = new ArrayList<>();
		// calculate rankings, in case multiple users have the same number of completed quests
		int ranking = 1;
		rankings.add(ranking);
		for(int i = 1; i < users.size(); i++) {
			if(users.get(i).getQuests() < users.get(i - 1).getQuests()) {
				rankings.add(++ranking);
			} else {
				rankings.add(ranking);
			}
		}
		model.addAttribute("users", users);
		model.addAttribute("rankings", rankings);
		
		return "leaderboard/leaderboard";
	}
	
}
