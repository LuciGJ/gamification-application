package com.luci.gamification.entity;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "badge")
public class Badge {

	// map Badge objects to badge table

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "picture")
	private String picture;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "users_badges", joinColumns = @JoinColumn(name = "badge_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Collection<User> users;

	// constructors

	public Badge() {

	}

	public Badge(String name, String description, String picture) {
		this.name = name;
		this.description = description;
		this.picture = picture;
	}

	// getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
