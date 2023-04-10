package com.luci.gamification;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.luci.gamification.badge.GamificationBadge;
import com.luci.gamification.quest.GamificationQuest;
import com.luci.gamification.submission.GamificationSubmission;
import com.luci.gamification.user.GamificationUser;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ValidationTests {

	private Validator validator;

	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testUserFailureEmail() {
		GamificationUser user = new GamificationUser();
		user.setEmail("test");
		user.setPassword("test");
		user.setConfirmPassword("test");
		user.setUsername("test");
		Set<ConstraintViolation<GamificationUser>> violations = validator.validate(user);
		assertThat(violations.isEmpty()).isFalse();
	}

	@Test
	public void testUserFailureWeakPassword() {
		GamificationUser user = new GamificationUser();
		user.setEmail("test@email.com");
		user.setPassword("test");
		user.setConfirmPassword("test");
		user.setUsername("test");
		Set<ConstraintViolation<GamificationUser>> violations = validator.validate(user);
		assertThat(violations.isEmpty()).isFalse();
	}

	@Test
	public void testUserFailurePasswordsDontMatch() {
		GamificationUser user = new GamificationUser();
		user.setEmail("test@email.com");
		user.setPassword("Test1234!");
		user.setConfirmPassword("test");
		user.setUsername("test");
		Set<ConstraintViolation<GamificationUser>> violations = validator.validate(user);
		assertThat(violations.isEmpty()).isFalse();
	}

	@Test
	public void testUserFailureUsernameNull() {
		GamificationUser user = new GamificationUser();
		user.setEmail("test@email.com");
		user.setPassword("Test1234!");
		user.setConfirmPassword("Test1234!");
		user.setUsername(null);
		Set<ConstraintViolation<GamificationUser>> violations = validator.validate(user);
		assertThat(violations.isEmpty()).isFalse();
	}

	@Test
	public void testUserSuccess() {
		GamificationUser user = new GamificationUser();
		user.setEmail("test@email.com");
		user.setPassword("Test1234!");
		user.setConfirmPassword("Test1234!");
		user.setUsername("test");
		Set<ConstraintViolation<GamificationUser>> violations = validator.validate(user);
		assertThat(violations.isEmpty()).isTrue();
	}

	@Test
	public void testBadgeFailNameNull() {
		GamificationBadge badge = new GamificationBadge();
		badge.setBadgeName(null);
		badge.setBadgeDescription("test");
		Set<ConstraintViolation<GamificationBadge>> violations = validator.validate(badge);
		assertThat(violations.isEmpty()).isFalse();
	}

	@Test
	public void testBadgeSuccess() {
		GamificationBadge badge = new GamificationBadge();
		badge.setBadgeName("test");
		badge.setBadgeDescription("test");
		Set<ConstraintViolation<GamificationBadge>> violations = validator.validate(badge);
		assertThat(violations.isEmpty()).isTrue();
	}

	@Test
	public void testQuestFailureNotEnoughTokens() {
		GamificationQuest quest = new GamificationQuest();
		quest.setName("test");
		quest.setDescription("test");
		quest.setAnswer("test");
		quest.setTokens(9);
		Set<ConstraintViolation<GamificationQuest>> violations = validator.validate(quest);
		assertThat(violations.isEmpty()).isFalse();
	}

	@Test
	public void testQuestFailureNameNull() {
		GamificationQuest quest = new GamificationQuest();
		quest.setName(null);
		quest.setDescription("test");
		quest.setAnswer("test");
		quest.setTokens(10);
		Set<ConstraintViolation<GamificationQuest>> violations = validator.validate(quest);
		assertThat(violations.isEmpty()).isFalse();
	}

	@Test
	public void testQuestSuccess() {
		GamificationQuest quest = new GamificationQuest();
		quest.setName("test");
		quest.setDescription("test");
		quest.setAnswer("test");
		quest.setTokens(10);
		Set<ConstraintViolation<GamificationQuest>> violations = validator.validate(quest);
		assertThat(violations.isEmpty()).isTrue();
	}

	@Test
	public void testSubmissionFailureAnswerNull() {
		GamificationSubmission submission = new GamificationSubmission();
		submission.setSubmissionAnswer(null);
		Set<ConstraintViolation<GamificationSubmission>> violations = validator.validate(submission);
		assertThat(violations.isEmpty()).isFalse();
	}

	@Test
	public void testSubmissionSuccess() {
		GamificationSubmission submission = new GamificationSubmission();
		submission.setSubmissionAnswer("test");
		Set<ConstraintViolation<GamificationSubmission>> violations = validator.validate(submission);
		assertThat(violations.isEmpty()).isTrue();
	}
}
