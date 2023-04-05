package com.luci.gamification.service;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luci.gamification.badge.GamificationBadge;
import com.luci.gamification.dao.BadgeDAO;
import com.luci.gamification.entity.Badge;

@Service
public class BadgeServiceImpl implements BadgeService {

	// the implementation of the BadgeService interface
	
	@Autowired
	BadgeDAO badgeDAO;

	
	// delegate methods
	
	@Transactional
	@Override
	public Badge findBadgeByName(String name) {
		return badgeDAO.findBadgeByName(name);
	}

	@Transactional
	@Override
	public Badge findBadgeById(int id) {
		return badgeDAO.findBadgeById(id);
	}

	@Transactional
	@Override
	public Badge save(GamificationBadge newBadge) {
		Badge badge = new Badge();
		badge.setName(newBadge.getBadgeName());
		badge.setDescription(newBadge.getBadgeDescription());
		if(newBadge.getPicture() == null) {
			badge.setPicture("badge.png");
		} else {
			badge.setPicture(newBadge.getPicture());
		}
		return badgeDAO.save(badge);
		
	}

	@Transactional
	@Override
	public Badge update(Badge badge) {
		return badgeDAO.save(badge);
	}

	@Transactional
	@Override
	public void delete(Badge badge) {
		badgeDAO.delete(badge);
		
	}
	

}
