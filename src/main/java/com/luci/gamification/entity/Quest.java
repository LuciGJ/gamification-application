package com.luci.gamification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "quest")
public class Quest {

	// map Quest objects to quest table
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "answer")
	private String answer;
	
	@Column(name = "tokens")
	private int tokens;
	
	@Column(name = "badge_id")
	private int badgeId;
	
	@Column(name = "approved")
	private int approved;
	
	
	// constructors
	
	public Quest() {

	}


	public Quest(String name, String description, String answer, int tokens, int badgeId, int approved) {
		this.name = name;
		this.description = description;
		this.answer = answer;
		this.tokens = tokens;
		this.badgeId = badgeId;
		this.approved = approved;
	}

	
	// getters and setters

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public int getTokens() {
		return tokens;
	}


	public void setTokens(int tokens) {
		this.tokens = tokens;
	}


	public int getBadgeId() {
		return badgeId;
	}


	public void setBadgeId(int badgeId) {
		this.badgeId = badgeId;
	}


	public int getApproved() {
		return approved;
	}


	public void setApproved(int approved) {
		this.approved = approved;
	}


	
	
}
