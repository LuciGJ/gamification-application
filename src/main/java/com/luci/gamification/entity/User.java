package com.luci.gamification.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	// map User objects to user table

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "enabled")
	private int enabled;

	@Column(name = "suspended")
	private int suspended;

	@Column(name = "email_token")
	private String emailToken;

	@Column(name = "password_token")
	private String passwordToken;

	@Column(name = "confirmation_token")
	private String confirmationToken;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_detail_id")
	private UserDetail userDetail;

	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;

	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinTable(name = "users_badges", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "badge_id"))
	private List<Badge> badges;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Quest> questList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Submission> submissionList;

	// constructors

	public User() {
	}

	public User(String username, String password, String email, int enabled, int suspended, String emailToken,
			String passwordToken, String confirmationToken, UserDetail userDetail, Collection<Role> roles) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.suspended = suspended;
		this.emailToken = emailToken;
		this.passwordToken = passwordToken;
		this.confirmationToken = confirmationToken;
		this.userDetail = userDetail;
		this.roles = roles;
	}

	// getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public int getSuspended() {
		return suspended;
	}

	public void setSuspended(int suspended) {
		this.suspended = suspended;
	}

	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public String getPasswordToken() {
		return passwordToken;
	}

	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public Collection<Badge> getBadges() {
		return badges;
	}

	public void setBadges(List<Badge> badges) {
		this.badges = badges;
	}

	public List<Quest> getQuestList() {
		return questList;
	}

	public void setQuestList(List<Quest> questList) {
		this.questList = questList;
	}

	public List<Submission> getSubmissionList() {
		return submissionList;
	}

	public void setSubmissionList(List<Submission> submissionList) {
		this.submissionList = submissionList;
	}

	// methods to add and remove objects in lists

	public void addQuest(Quest quest) {
		if (questList == null) {
			questList = new ArrayList<>();
		}

		questList.add(quest);

	}

	public void removeQuest(int index) {
		questList.remove(index);
	}

	public void addBadge(Badge badge) {
		if (badges == null) {
			badges = new ArrayList<>();
		}

		badges.add(badge);

	}

	public void removeBadge(int index) {
		badges.remove(index);
	}

	public void addSubmission(Submission submission) {
		if (submissionList == null) {
			submissionList = new ArrayList<>();
		}

		submissionList.add(submission);

	}

	public void removeSubmission(int index) {
		submissionList.remove(index);
	}

}
