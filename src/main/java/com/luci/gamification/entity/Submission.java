package com.luci.gamification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "submission")
public class Submission {

	// map Submission objects to submission table

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "quest_id")
	private int questId;

	@Column(name = "answer")
	private String answer;

	@Column(name = "status")
	private int status;

	@Column(name = "submit_id")
	private int submitId;

	// constructors

	public Submission() {

	}

	public Submission(int questId, String answer, int status) {
		this.questId = questId;
		this.answer = answer;
		this.status = status;
	}

	// getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSubmitId() {
		return submitId;
	}

	public void setSubmitId(int submitId) {
		this.submitId = submitId;
	}

}
