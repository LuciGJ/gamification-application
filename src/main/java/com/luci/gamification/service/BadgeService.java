package com.luci.gamification.service;

import java.util.List;

import com.luci.gamification.badge.GamificationBadge;
import com.luci.gamification.entity.Badge;


public interface BadgeService {
	// interface that groups together methods related to badges
	public Badge findBadgeByName(String name);
	
	public Badge findBadgeById(int id);
	
	public Badge save(GamificationBadge newBadge);
	
	public Badge update(Badge badge);
	
	public void delete(Badge badge);
}
