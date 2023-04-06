package com.luci.gamification.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luci.gamification.entity.Badge;

import jakarta.persistence.EntityManager;

@Repository
public class BadgeDAOImpl implements BadgeDAO {

	// the implementation of the BadgeDAO interface
	// similar to the logic used in the UserDAOImpl class
	@Autowired
	EntityManager entityManager;

	@Override
	public Badge findBadgeByName(String name) {
		Session session = entityManager.unwrap(Session.class);
		Query<Badge> query = session.createQuery("from Badge where name = :name", Badge.class);

		query.setParameter("name", name);

		Badge badge;
		try {
			badge = query.getSingleResult();
		} catch (Exception e) {
			badge = null;
		}
		return badge;
	}

	@Override
	public Badge findBadgeById(int id) {
		Session session = entityManager.unwrap(Session.class);
		Query<Badge> query = session.createQuery("from Badge where id = :id", Badge.class);

		query.setParameter("id", id);

		Badge badge;
		try {
			badge = query.getSingleResult();
		} catch (Exception e) {
			badge = null;
		}
		return badge;
	}

	@Override
	public Badge save(Badge badge) {
		Session session = entityManager.unwrap(Session.class);
		return session.merge(badge);

	}

	@Override
	public void delete(Badge badge) {
		String badgesDir = "badge-photos/";

		try {
			Path deleteFile;
			deleteFile = Paths.get(badgesDir + badge.getId() + ".png");
			Files.deleteIfExists(deleteFile);
			deleteFile = Paths.get(badgesDir + badge.getId() + ".jpg");
			Files.deleteIfExists(deleteFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
		Session session = entityManager.unwrap(Session.class);
		session.remove(badge);
	}

}
