package com.luci.gamification.quest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GamificationQuest {

	// class used to validate the quest input during quest creation

	@NotNull(message = "Name is required")
	@Size(min = 1, max = 30, message = "Please choose a name between 1 and 30 characters")
	private String name;

	@NotNull(message = "Description is required")
	@Size(min = 1, max = 200, message = "Please choose a description between 1 and 200 characters")
	private String description;

	@NotNull(message = "Answer is required")
	@Size(min = 1, max = 200, message = "Please choose an answer between 1 and 200 characters")
	private String answer;

	@NotNull(message = "Reward is required")
	@Min(value = 10, message = "At least 10 tokens required")
	private int tokens;

	// constructor

	public GamificationQuest() {

	}

	// getters and setters

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

}
