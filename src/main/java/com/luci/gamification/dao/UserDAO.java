package com.luci.gamification.dao;

import java.util.List;

import com.luci.gamification.entity.Quest;
import com.luci.gamification.entity.User;
import com.luci.gamification.entity.UserDetail;

public interface UserDAO {

	// interface for grouping methods related to user

	public User findUserByUsername(String username);

	public User findUserById(int id);

	public void save(User user);

	public void delete(User user);

	public UserDetail findUserDetailByDisplayName(String displayName);

	public User findUserByEmail(String email);

	public User findByResetPasswordToken(String token);

	public User findByChangeEmailToken(String token);

	public User findByConfirmationToken(String token);

	public List<User> findAllUsers(String username);

	public List<User> searchUser(String username);

	public void updateDetail(UserDetail detail);

	public UserDetail findDetailById(int id);

	public int getUserIdFromQuest(Quest quest);

	public List<UserDetail> getUsersByQuests();
}
