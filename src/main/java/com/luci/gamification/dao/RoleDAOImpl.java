package com.luci.gamification.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luci.gamification.entity.Role;

import jakarta.persistence.EntityManager;

@Repository
public class RoleDAOImpl implements RoleDAO {

	// the implementation of the RoleDAO interface

	@Autowired
	EntityManager entityManager;

	// similar to the logic used in the UserDAOImpl class
	// get a session, create a parameterized query and return the result
	@Override
	public Role findRoleByName(String role) {
		Session session = entityManager.unwrap(Session.class);
		Query<Role> query = session.createQuery("from Role where name = :name", Role.class);
		query.setParameter("name", role);

		Role theRole;

		try {
			theRole = query.getSingleResult();
		} catch (Exception e) {
			theRole = null;
		}

		return theRole;
	}

}
