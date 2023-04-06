package com.luci.gamification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luci.gamification.entity.Badge;
import com.luci.gamification.entity.Quest;
import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.service.BadgeService;
import com.luci.gamification.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	UserService userService;
	
	@Autowired
	BadgeService badgeService;
	
	@GetMapping("/viewProfile")
	public String viewProfile(@RequestParam("displayName") String displayName, Model model) {
		
		// get the user details using the display name
		UserDetail userDetail = userService.findDetailByDisplayName(displayName);
		
		// if null then the profile does not exist, redirect to leaderboard
		
		if(userDetail == null) {
			return "redirect:/leaderboard/viewLeaderboard";
		}
		
		// get displayed badge
		
		Badge badge = badgeService.findBadgeById(userDetail.getBadgeId());
		
		model.addAttribute("userDetail", userDetail);
		model.addAttribute("badge", badge);
		
		return "profile/profile";
		

	}
	
	
}
