package com.luci.gamification.service;

import java.util.List;


import com.luci.gamification.entity.Submission;


public interface SubmissionService {
	// interface that groups together methods related to submissions
	public List<Submission> findSubmissionsByQuest(int questId);
	
	public void save(Submission submission);
	
	public Submission findSubmissionById(int submissionId);
}
