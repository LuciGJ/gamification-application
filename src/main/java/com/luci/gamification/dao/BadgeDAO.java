package com.luci.gamification.dao;

import java.util.List;

import com.luci.gamification.entity.Badge;




public interface BadgeDAO {

	// interface for grouping methods related to badges
	public Badge findBadgeByName(String name);
	
	public Badge findBadgeById(int id);
	
	public Badge save(Badge badge);
	
	public void delete(Badge badge);


}
