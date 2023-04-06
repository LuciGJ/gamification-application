package com.luci.gamification.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luci.gamification.account.EmailDetails;
import com.luci.gamification.dao.RoleDAO;
import com.luci.gamification.dao.UserDAO;
import com.luci.gamification.entity.Quest;
import com.luci.gamification.entity.Role;
import com.luci.gamification.entity.User;
import com.luci.gamification.entity.UserDetail;
import com.luci.gamification.user.GamificationUser;
import com.luci.gamification.utility.LinkUtility;
import com.luci.gamification.utility.RandomStringBuilder;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

	// the implementation of the UserService interface

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// delegate the method to userDAO
	@Override
	@Transactional
	public User findUserByUsername(String username) {
		return userDAO.findUserByUsername(username);
	}

	// method used to register an user
	@Override
	@Transactional
	public void saveUser(GamificationUser user, EmailService emailService, HttpServletRequest request) {

		// create a new user using the input received in the GamificationUser object
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		UserDetail userDetail = new UserDetail();
		userDetail.setImage("default.png");
		userDetail.setDisplayName(user.getUsername());
		userDetail.setQuests(0);
		userDetail.setTokens(0);
		newUser.setUserDetail(userDetail);
		newUser.setEnabled(0);
		newUser.setSuspended(0);

		// set the role to regular user

		newUser.setRoles(Arrays.asList(roleDAO.findRoleByName("ROLE_USER")));

		// create a confirmation token and send the link to the user's email

		String token;
		do {
			token = RandomStringBuilder.buildRandomString(30);
		} while (findByConfirmationToken(token) != null);
		newUser.setConfirmationToken(token);
		String confirmationLink = LinkUtility.getSiteURL(request) + "/confirmAccount?token=" + token;
		EmailDetails details = new EmailDetails();

		details.setRecipient(user.getEmail());
		details.setMsgBody("Click the following link to confirm your account: \n " + confirmationLink);
		details.setSubject("Gamification account confirmation");
		emailService.sendEmail(details);

		userDAO.save(newUser);

	}

	// used to authenticate the user, checks if the account is confirmed or
	// suspended
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDAO.findUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password");
		}

		if (user.getEnabled() == 0) {
			throw new DisabledException("Account not activated");
		}

		if (user.getSuspended() == 1) {
			throw new LockedException("Account suspended");
		}

		// return the user with the username, password and role (mapped from Role to
		// GrantedAuthority)
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	// used to convert from Role to GrantedAuthority by mapping each role to a
	// SimpleGrantedAuthority and collecting them to a list
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	// delegate methods to userDAO
	@Override
	@Transactional
	public void updateUser(User user) {
		userDAO.save(user);
	}

	@Override
	@Transactional
	public User findUserByEmail(String email) {
		return userDAO.findUserByEmail(email);
	}

	// set tokens

	@Transactional
	@Override
	public void updateResetPasswordToken(String token, String email) {
		User user = findUserByEmail(email);
		user.setPasswordToken(token);
		updateUser(user);
	}

	@Transactional
	@Override
	public void updateChangeEmailToken(String token, String email) {
		User user = findUserByEmail(email);
		user.setEmailToken(token);
		updateUser(user);

	}

	@Transactional
	@Override
	public void updateConfirmationToken(String token, String username) {
		User user = findUserByEmail(username);
		user.setConfirmationToken(token);
		updateUser(user);

	}

	// delegate methods to userDAO
	@Override
	public User findByResetPasswordToken(String token) {
		return userDAO.findByResetPasswordToken(token);
	}

	@Override
	public User findByChangeEmailToken(String token) {
		return userDAO.findByChangeEmailToken(token);
	}

	@Override
	public User findByConfirmationToken(String token) {
		return userDAO.findByConfirmationToken(token);
	}

	// update password, use BCryptPasswordEncoder for encoding
	@Transactional
	@Override
	public void updatePassword(String username, String password) {
		User user = findUserByUsername(username);
		String encodedPassword = passwordEncoder.encode(password);
		user.setPassword(encodedPassword);

		user.setPasswordToken(null);
		updateUser(user);

	}

	@Transactional
	@Override
	public void remove(User user) {
		userDAO.delete(user);

	}

	@Transactional
	@Override
	public List<User> findAllUsers(String username) {
		return userDAO.findAllUsers(username);
	}

	@Transactional
	@Override
	public User findUserById(int id) {
		return userDAO.findUserById(id);
	}

	// search the user in the list, if the username is null or empty return all
	// users
	@Override
	public List<User> searchUser(String currentUsername, String username) {

		List<User> results = null;

		if (username != null && (username.trim().length() > 0)) {
			results = userDAO.searchUser(username);
		} else {
			results = findAllUsers(currentUsername);
		}

		return results;
	}

	@Transactional
	@Override
	public UserDetail findDetailByDisplayName(String displayName) {
		return userDAO.findUserDetailByDisplayName(displayName);
	}

	@Transactional
	@Override
	public void updateUserDetail(UserDetail detail) {
		userDAO.updateDetail(detail);
	}

	@Transactional
	@Override
	public UserDetail findDetailById(int id) {
		return userDAO.findDetailById(id);
	}

	@Transactional
	@Override
	public int getUserIdFromQuest(Quest quest) {
		return userDAO.getUserIdFromQuest(quest);
	}

	@Transactional
	@Override
	public List<UserDetail> getUsersByQuests() {
		return userDAO.getUsersByQuests();
	}

}
