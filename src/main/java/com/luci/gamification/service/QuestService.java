package com.luci.gamification.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luci.gamification.entity.Badge;
import com.luci.gamification.entity.Quest;
import com.luci.gamification.quest.GamificationQuest;

public interface QuestService {
	// interface that groups together methods related to quests
	public List<Quest> findQuestsByApproval(boolean approved);

	public List<Quest> findQuestsByCreator(int creatorId);

	public Quest findQuestById(int questId);

	public void save(GamificationQuest newQuest, Badge badge);

	public void delete(Quest quest);

	public Quest findQuestByName(String name);

	public void update(Quest quest);
	
	public Page<Quest> findPaginated(Pageable pageable);
}
