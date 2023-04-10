package com.luci.gamification.dao;

import java.util.List;

import com.luci.gamification.entity.Quest;

public interface QuestDAO {

	// interface for grouping methods related to quests
	public List<Quest> findQuestsByApproval(boolean approved, int id);

	public List<Quest> findQuestsByApproval(boolean approved);
	
	public List<Quest> findQuestsByCreator(int creatorId);

	public Quest findQuestById(int questId);

	public Quest findQuestByName(String name);

	public void save(Quest quest);

	public void delete(Quest quest);
	
	public List<Quest> searchQuest(String name, int id);

}
