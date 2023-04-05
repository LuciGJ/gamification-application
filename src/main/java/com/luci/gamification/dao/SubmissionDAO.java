package com.luci.gamification.dao;

import java.util.List;

import com.luci.gamification.entity.Submission;



public interface SubmissionDAO {

	// interface for grouping methods related to submissions
	public List<Submission> findSubmissionsByQuest(int questId);
	
	public void save(Submission submission);


}
