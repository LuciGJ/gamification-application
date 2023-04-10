package com.luci.gamification.utility;

public class QuestFilter {

	// helper class to filter quests

	private boolean submitted;
	private boolean notSubmitted;
	private boolean accepted;
	private boolean rejected;
	private String keyword;
	private String submittedString;
	private String notSubmittedString;
	private String acceptedString;
	private String rejectedString;

	// constructor
	public QuestFilter() {
		this.submitted = true;
		this.notSubmitted = true;
		this.accepted = true;
		this.rejected = true;
		submittedString = "on";
		notSubmittedString = "on";
		acceptedString = "on";
		rejectedString = "on";
	}

	// methods to set the boolean values based on strings retrieved from the query
	public void setSubmittedByString(String str) {
		if (str.equals("")) {
			this.submitted = false;
		} else if (str.equals("on")) {
			this.submitted = true;
		}
		this.submittedString = str;
	}

	public void setNotSubmittedByString(String str) {
		if (str.equals("")) {
			this.notSubmitted = false;
		} else if (str.equals("on")) {
			this.notSubmitted = true;
		}
		this.notSubmittedString = str;
	}

	public void setAcceptedByString(String str) {
		if (str.equals("")) {
			this.accepted = false;
		} else if (str.equals("on")) {
			this.accepted = true;
		}
		this.acceptedString = str;
	}

	public void setRejectedByString(String str) {
		if (str.equals("")) {
			this.rejected = false;
		} else if (str.equals("on")) {
			this.rejected = true;
		}
		this.rejectedString = str;
	}

	// getters and setters
	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public boolean isNotSubmitted() {
		return notSubmitted;
	}

	public void setNotSubmitted(boolean notSubmitted) {
		this.notSubmitted = notSubmitted;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSubmittedString() {
		return submittedString;
	}

	public void setSubmittedString(String submittedString) {
		this.submittedString = submittedString;
	}

	public String getNotSubmittedString() {
		return notSubmittedString;
	}

	public void setNotSubmittedString(String notSubmittedString) {
		this.notSubmittedString = notSubmittedString;
	}

	public String getAcceptedString() {
		return acceptedString;
	}

	public void setAcceptedString(String acceptedString) {
		this.acceptedString = acceptedString;
	}

	public String getRejectedString() {
		return rejectedString;
	}

	public void setRejectedString(String rejectedString) {
		this.rejectedString = rejectedString;
	}

}
