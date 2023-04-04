package com.luci.gamification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_detail")
public class UserDetail {

	// map UserDetail objects to user_detail table
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "description")
	private String description;

	@Column(name = "badge_id")
	private int badgeId;
	
	@Column(name = "tokens")
	private int tokens;
	
	@Column(name = "quests")
	private int quests;
	
	@Column(name = "image")
	private String image;

	// constructors
	
	public UserDetail() {
	}

	public UserDetail(String displayName, String description, int badgeId, int tokens, int quests, String image) {
		this.displayName = displayName;
		this.description = description;
		this.badgeId = badgeId;
		this.tokens = tokens;
		this.quests = quests;
		this.image = image;
	}

	// getters and setters
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getBadgeId() {
		return badgeId;
	}

	public void setBadgeId(int badgeId) {
		this.badgeId = badgeId;
	}

	public int getTokens() {
		return tokens;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	public int getQuests() {
		return quests;
	}

	public void setQuests(int quests) {
		this.quests = quests;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	
}
