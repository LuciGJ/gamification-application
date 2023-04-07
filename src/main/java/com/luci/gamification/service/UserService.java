package com.luci.gamification.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.luci.gamification.entity.Quest;
import com.luci.gamification.entity.User;
import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.user.GamificationUser;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService {

	// interface that groups together methods related to the user

	public User findUserByUsername(String username);

	public User findUserById(int id);

	public User findUserByEmail(String email);

	public void saveUser(GamificationUser user, EmailService emailService, HttpServletRequest request);

	public void updateUser(User user);

	public void updateResetPasswordToken(String token, String email);

	public void updateChangeEmailToken(String token, String email);

	public void updateConfirmationToken(String token, String username);

	public User findByResetPasswordToken(String token);

	public User findByChangeEmailToken(String token);

	public User findByConfirmationToken(String token);

	public UserDetail findDetailByDisplayName(String displayName);

	public void updatePassword(String username, String password);

	public void remove(User user);

	public List<User> findAllUsers(String username);

	public List<User> searchUser(String currentUsername, String username);

	public void updateUserDetail(UserDetail detail);

	public UserDetail findDetailById(int id);

	public List<UserDetail> getUsersByQuests();
}
