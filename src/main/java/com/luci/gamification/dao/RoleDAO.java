package com.luci.gamification.dao;

import com.luci.gamification.entity.Role;

public interface RoleDAO {

	// interface for defining the method for finding a role
	public Role findRoleByName(String role);

}
