package com.luci.gamification.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luci.gamification.dao.SubmissionDAO;
import com.luci.gamification.entity.Submission;

@Service
public class SubmissionServiceImpl implements SubmissionService {

	// the implementation of the SubmissionService interface
	
	@Autowired
	SubmissionDAO submissionDAO;

	// delegate methods
	
	@Transactional
	@Override
	public List<Submission> findSubmissionsByQuest(int questId) {
		return submissionDAO.findSubmissionsByQuest(questId);
	}

	@Transactional
	@Override
	public void save(Submission submission) {
		submissionDAO.save(submission);		
	}


	
	
	
	

}
