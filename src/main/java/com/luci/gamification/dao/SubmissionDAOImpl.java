package com.luci.gamification.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luci.gamification.entity.Submission;

import jakarta.persistence.EntityManager;

@Repository
public class SubmissionDAOImpl implements SubmissionDAO {

	// the implementation of the SubmissionDAO interface
	// similar to the logic used in the UserDAOImpl class
	@Autowired
	EntityManager entityManager;

	@Override
	public List<Submission> findSubmissionsByQuest(int questId) {
		Session session = entityManager.unwrap(Session.class);
		Query<Submission> query = session.createQuery("from Submission where questId = :id and status = 0",
				Submission.class);

		query.setParameter("id", questId);

		List<Submission> submissions;
		try {
			submissions = query.getResultList();
		} catch (Exception e) {
			submissions = new ArrayList<>();
		}
		return submissions;
	}

	@Override
	public void save(Submission submission) {
		Session session = entityManager.unwrap(Session.class);
		session.merge(submission);

	}

	@Override
	public Submission findSubmissionById(int submissionId) {
		Session session = entityManager.unwrap(Session.class);
		Query<Submission> query = session.createQuery("from Submission where id = :id", Submission.class);
		query.setParameter("id", submissionId);
		Submission submission;
		try {
			submission = query.getSingleResult();
		} catch (Exception e) {
			submission = null;
		}
		return submission;
	}

}
