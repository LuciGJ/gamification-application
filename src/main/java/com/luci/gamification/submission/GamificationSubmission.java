package com.luci.gamification.submission;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GamificationSubmission {

	// class used to validate the submission input


	@NotNull(message = "Answer is required")
	@Size(min = 1, max = 200, message = "Please choose an answer between 1 and 200 characters")
	private String submissionAnswer;

	
	private boolean exists;
	
	int questId;
	
	int userId;
	

	// constructor
	
	public GamificationSubmission() {

	}



	// getters and setters

	public String getSubmissionAnswer() {
		return submissionAnswer;
	}


	public void setSubmissionAnswer(String submissionAnswer) {
		this.submissionAnswer = submissionAnswer;
	}


	public boolean isExists() {
		return exists;
	}


	public void setExists(boolean exists) {
		this.exists = exists;
	}


	public int getQuestId() {
		return questId;
	}


	public void setQuestId(int questId) {
		this.questId = questId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}

	
	
}
