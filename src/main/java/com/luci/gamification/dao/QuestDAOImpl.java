package com.luci.gamification.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luci.gamification.entity.Quest;

import jakarta.persistence.EntityManager;

@Repository
public class QuestDAOImpl implements QuestDAO {

	// the implementation of the QuestDAO interface
	// similar to the logic used in the UserDAOImpl class
	@Autowired
	EntityManager entityManager;

	@Override
	public List<Quest> findQuestsByApproval(boolean approved, int id) {
		Session session = entityManager.unwrap(Session.class);
		Query<Quest> query = session.createQuery("from Quest where approved = :approved and creatorId != :id",
				Quest.class);
		// set approved based on the boolean's value
		if (approved) {
			query.setParameter("approved", 1);
		} else {
			query.setParameter("approved", 0);
		}

		// get the quests that are not created by the current user

		query.setParameter("id", id);

		// get the list of quests
		List<Quest> quests;
		try {
			quests = query.getResultList();
		} catch (Exception e) {
			quests = new ArrayList<>();
		}
		return quests;
	}

	@Override
	public List<Quest> findQuestsByApproval(boolean approved) {
		Session session = entityManager.unwrap(Session.class);
		Query<Quest> query = session.createQuery("from Quest where approved = :approved", Quest.class);
		// set approved based on the boolean's value
		if (approved) {
			query.setParameter("approved", 1);
		} else {
			query.setParameter("approved", 0);
		}

		// get the list of quests
		List<Quest> quests;
		try {
			quests = query.getResultList();
		} catch (Exception e) {
			quests = new ArrayList<>();
		}
		return quests;
	}

	@Override
	public List<Quest> findQuestsByCreator(int creatorId) {
		Session session = entityManager.unwrap(Session.class);
		Query<Quest> query = session.createQuery("from Quest where creatorId = :id", Quest.class);

		query.setParameter("id", creatorId);

		List<Quest> quests;
		try {
			quests = query.getResultList();
		} catch (Exception e) {
			quests = new ArrayList<>();
		}
		return quests;
	}

	@Override
	public Quest findQuestById(int questId) {
		Session session = entityManager.unwrap(Session.class);
		Query<Quest> query = session.createQuery("from Quest where id = :id", Quest.class);
		query.setParameter("id", questId);
		Quest quest;
		try {
			quest = query.getSingleResult();
		} catch (Exception e) {
			quest = null;
		}
		return quest;
	}

	@Override
	public void save(Quest quest) {
		Session session = entityManager.unwrap(Session.class);
		session.merge(quest);

	}

	@Override
	public void delete(Quest quest) {
		Session session = entityManager.unwrap(Session.class);
		session.remove(quest);

	}

	@Override
	public Quest findQuestByName(String name) {
		Session session = entityManager.unwrap(Session.class);
		Query<Quest> query = session.createQuery("from Quest where name = :name", Quest.class);
		query.setParameter("name", name);
		Quest quest;
		try {
			quest = query.getSingleResult();
		} catch (Exception e) {
			quest = null;
		}
		return quest;
	}

	// get all the quests that match the name and are not created by the current
	// user
	@Override
	public List<Quest> searchQuest(String name, int id) {
		name = "%" + name.toLowerCase() + "%";
		Session session = entityManager.unwrap(Session.class);
		Query<Quest> query = session.createQuery(
				"from Quest where lower(name) like :name and approved = 1 and creatorId != :id", Quest.class);
		query.setParameter("name", name);
		query.setParameter("id", id);
		List<Quest> quests;
		try {
			quests = query.getResultList();
		} catch (Exception e) {
			quests = new ArrayList<>();
		}
		return quests;
	}

}
