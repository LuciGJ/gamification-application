package com.luci.gamification;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.luci.gamification.dao.BadgeDAO;
import com.luci.gamification.dao.QuestDAO;
import com.luci.gamification.dao.SubmissionDAO;
import com.luci.gamification.dao.UserDAO;
import com.luci.gamification.entity.Badge;
import com.luci.gamification.entity.Quest;
import com.luci.gamification.entity.Submission;
import com.luci.gamification.entity.User;
import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.service.BadgeService;
import com.luci.gamification.service.QuestService;
import com.luci.gamification.service.SubmissionService;
import com.luci.gamification.service.UserService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)

class ServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private QuestService questService;

	@Autowired
	private SubmissionService submissionService;

	@Autowired
	private BadgeService badgeService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@MockBean
	private UserDAO userDAO;

	@MockBean
	private QuestDAO questDAO;

	@MockBean
	private BadgeDAO badgeDAO;

	@MockBean
	private SubmissionDAO submissionDAO;

	@BeforeEach
	public void setUp() {
		User user = new User();
		user.setUsername("test");
		user.setEmail("test@email.com");
		user.setPassword(passwordEncoder.encode("Test1234!"));
		UserDetail userDetail = new UserDetail();
		userDetail.setDisplayName(user.getUsername());
		userDetail.setDescription("test");
		user.setUserDetail(userDetail);
		user.setEnabled(0);

		Quest quest = new Quest();
		Badge badge = new Badge();
		badge.setName("test badge");
		badge.setDescription("test description");
		badge.setPicture("test.png");
		quest.setName("test");
		quest.setAnswer("test answer");
		quest.setDescription("test description");
		quest.setCreatorId(user.getId());
		quest.setBadgeId(badge.getId());
		user.addQuest(quest);
		user.addBadge(badge);
		Submission submission = new Submission();
		submission.setAnswer("test answer");
		submission.setQuestId(quest.getId());
		submission.setSubmitId(user.getId());
		submission.setStatus(0);
		user.addSubmission(submission);
		List<Submission> submissions = new ArrayList<>();
		submissions.add(submission);

		Mockito.when(userDAO.findUserByUsername("test")).thenReturn(user);
		Mockito.when(questDAO.findQuestByName("test")).thenReturn(quest);
		Mockito.when(submissionDAO.findSubmissionsByQuest(quest.getId())).thenReturn(submissions);
		Mockito.when(badgeDAO.findBadgeById(quest.getBadgeId())).thenReturn(badge);
	}

	@Test
	public void findUserByUsernameValid() {
		User user = userService.findUserByUsername("test");

		assertThat(user.getUsername()).isEqualTo("test");
	}

	@Test
	public void findUserByUsernameNotValid() {
		User user = userService.findUserByUsername("N/A");
		assertThat(user).isNull();
	}

	@Test
	public void getUserQuests() {
		User user = userService.findUserByUsername("test");
		List<Quest> quests = user.getQuestList();
		assertThat(quests.size()).isGreaterThan(0);
	}

	@Test
	public void getUserBadges() {
		User user = userService.findUserByUsername("test");
		List<Badge> badges = (List<Badge>) user.getBadges();
		assertThat(badges.size()).isGreaterThan(0);
	}

	@Test
	public void getUserSubmissions() {
		User user = userService.findUserByUsername("test");
		List<Submission> submissions = user.getSubmissionList();
		assertThat(submissions.size()).isGreaterThan(0);
	}

	@Test
	public void getSubmissionsByQuest() {
		Quest quest = questService.findQuestByName("test");
		List<Submission> submissions = submissionService.findSubmissionsByQuest(quest.getId());
		assertThat(submissions.size()).isGreaterThan(0);
	}

	@Test
	public void getQuestBadge() {
		Quest quest = questService.findQuestByName("test");
		Badge badge = badgeService.findBadgeById(quest.getBadgeId());
		assertThat(badge.getName()).isEqualTo("test badge");
	}

	@Test
	public void checkSubmissionAnswer() {
		Quest quest = questService.findQuestByName("test");
		List<Submission> submissions = submissionService.findSubmissionsByQuest(quest.getId());
		assertThat(submissions.get(0).getAnswer()).isEqualTo("test answer");
	}

	@Test
	public void checkDisplayName() {
		User user = userService.findUserByUsername("test");
		UserDetail userDetail = user.getUserDetail();
		assertThat(userDetail.getDisplayName()).isEqualTo("test");
	}
}
