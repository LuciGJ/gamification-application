package com.luci.gamification.badge;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GamificationBadge {

	// class used to validate the badge input during quest creation

	@NotNull(message = "Name is required")
	@Size(min = 1, max = 30, message = "Please choose a name between 1 and 30 characters")
	private String badgeName;

	@NotNull(message = "Description is required")
	@Size(min = 1, max = 200, message = "Please choose a description between 1 and 200 characters")
	private String badgeDescription;

	private String picture;

	private boolean activated;

	// constructor

	public GamificationBadge() {

	}

	// getters and setters

	public String getPicture() {
		return picture;
	}

	public String getBadgeName() {
		return badgeName;
	}

	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}

	public String getBadgeDescription() {
		return badgeDescription;
	}

	public void setBadgeDescription(String badgeDescription) {
		this.badgeDescription = badgeDescription;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

}
