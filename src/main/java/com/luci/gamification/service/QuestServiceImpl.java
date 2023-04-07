package com.luci.gamification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luci.gamification.dao.QuestDAO;
import com.luci.gamification.entity.Badge;
import com.luci.gamification.entity.Quest;
import com.luci.gamification.quest.GamificationQuest;

@Service
public class QuestServiceImpl implements QuestService {

	@Autowired
	QuestDAO questDAO;

	// the implementation of the QuestService interface

	// delegate methods

	@Transactional
	@Override
	public List<Quest> findQuestsByApproval(boolean approved) {
		return questDAO.findQuestsByApproval(approved);
	}

	@Transactional
	@Override
	public List<Quest> findQuestsByCreator(int creatorId) {
		return questDAO.findQuestsByCreator(creatorId);
	}

	@Transactional
	@Override
	public Quest findQuestById(int questId) {
		return questDAO.findQuestById(questId);
	}

	@Transactional
	@Override

	// set quest fields and save it
	public void save(GamificationQuest newQuest, Badge badge) {
		Quest quest = new Quest();
		quest.setName(newQuest.getName());
		quest.setDescription(newQuest.getDescription());
		quest.setAnswer(newQuest.getAnswer());
		quest.setBadgeId(badge.getId());
		quest.setTokens(newQuest.getTokens());
		quest.setApproved(0);
		questDAO.save(quest);

	}

	@Transactional
	@Override
	public void delete(Quest quest) {
		questDAO.delete(quest);
	}

	@Transactional
	@Override
	public Quest findQuestByName(String name) {
		return questDAO.findQuestByName(name);
	}

	@Transactional
	@Override
	public void update(Quest quest) {
		questDAO.save(quest);

	}
	
	@Override
	public Page<Quest> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Quest> list;
        List<Quest> quests = findQuestsByApproval(true);
        if (quests.size() < startItem) {
            list = new ArrayList<>();
        } else {
            int toIndex = Math.min(startItem + pageSize, quests.size());
            list = quests.subList(startItem, toIndex);
        }

        Page<Quest> bookPage
          = new PageImpl<Quest>(list, PageRequest.of(currentPage, pageSize), quests.size());

        return bookPage;
    }

	@Transactional
	@Override
	public List<Quest> searchQuests(String name) {
		List<Quest> results = null;
		
		if (name != null && (name.trim().length() > 0)) {
			results = questDAO.searchQuest(name);
		} else {
			results = questDAO.findQuestsByApproval(true);
		}

		return results;
	}

}
