package com.luci.gamification.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luci.gamification.entity.Quest;
import com.luci.gamification.entity.User;
import com.luci.gamification.entity.UserDetail;

import jakarta.persistence.EntityManager;

@Repository
public class UserDAOImpl implements UserDAO {

	// the implementation of the UserDAO interface
	
	
	// used to access the database
	@Autowired
	EntityManager entityManager;

	// methods used to find users by specific information or to manipulate them
	
	
	// the methods used for finding the user are similar
	// create a session, execute a parameterized query and return the result
	@Override
	public User findUserByUsername(String username) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where username = :username", User.class);
		query.setParameter("username", username);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}
		return user;
	}

	

	@Override
	public User findUserByEmail(String email) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where email = :email", User.class);
		query.setParameter("email", email);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}

	@Override
	public User findByResetPasswordToken(String token) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where passwordToken = :token", User.class);
		query.setParameter("token", token);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}

	@Override
	public User findByChangeEmailToken(String token) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where emailToken = :token", User.class);
		query.setParameter("token", token);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}

	@Override
	public User findByConfirmationToken(String token) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where confirmationToken = :token", User.class);
		query.setParameter("token", token);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}
	
	
	@Override
	public User findUserById(int id) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where id = :id", User.class);
		query.setParameter("id", id);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}
		return user;
	}
	
	// save the user by getting a session and calling merge on it (which will save or update the user)
	@Override
	public void save(User user) {
		Session session = entityManager.unwrap(Session.class);

		session.merge(user);

	}

	
	// method used to delete the user and the profile picture associated with it
	@Override
	public void delete(User user) {
		String picturesDir = "user-photos/";
	
		try {
			Path deleteFile;
			deleteFile = Paths.get(picturesDir + user.getId() + ".png");
			Files.deleteIfExists(deleteFile);
			deleteFile = Paths.get(picturesDir + user.getId() + ".jpg");
			Files.deleteIfExists(deleteFile);
		
		} catch (IOException e) {
			e.printStackTrace();
		}

		Session session = entityManager.unwrap(Session.class);

		session.remove(user);

	}

	
	
	// find all users expect the authenticated one
	@Override
	public List<User> findAllUsers(String username) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where username != :username", User.class);
		query.setParameter("username", username);
		List<User> users;
		try {
			users = query.getResultList();
		} catch (Exception e) {
			users = new ArrayList<>();
		}
		return users;
	}


	// method used to search users by username
	// the username is placed between %, which means that it will return the results that contain the username (by using the like function, % means anything before and after the username)
	
	@Override
	public List<User> searchUser(String username) {
		username = "%" + username.toLowerCase() + "%";
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where lower(username) like :username", User.class);
		query.setParameter("username", username);
		List<User> users;
		try {
			users = query.getResultList();
		} catch (Exception e) {
			users = new ArrayList<>();
		}
		return users;
	}


	// find user by display name
	@Override
	public UserDetail findUserDetailByDisplayName(String displayName) {
		Session session = entityManager.unwrap(Session.class);
		Query<UserDetail> query = session.createQuery("from UserDetail where displayName = :displayName", UserDetail.class);
		query.setParameter("displayName", displayName);
		UserDetail user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}



	@Override
	public void updateDetail(UserDetail detail) {
		Session session = entityManager.unwrap(Session.class);
		
		session.merge(detail);
		
	}



	@Override
	public UserDetail findDetailById(int id) {
		Session session = entityManager.unwrap(Session.class);
		Query<UserDetail> query = session.createQuery("from UserDetail where id = :id", UserDetail.class);
		query.setParameter("id", id);
		UserDetail user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}



	@Override
	public int getUserIdFromQuest(Quest quest) {
		Session session = entityManager.unwrap(Session.class);
		Query<Integer> query =  session.createQuery("select quest.userId from Quest quest where quest.id = : id", Integer.class);
		query.setParameter("id", quest.getId());
		int id;
		try {
			id = query.getSingleResult();
		} catch (Exception e) {
			id = 0;
		}
		
		return id;
	}

}
