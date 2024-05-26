package com.smartcontactmanager.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.*;

@Entity
@Table(name = "USER")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	@Column(unique = true)
	private String email;
	private String role;
	private String password;
	private boolean activeFl;
	private String imageUrl;
	@Column(length = 500)
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY , mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isActiveFl() {
		return activeFl;
	}
	public void setActiveFl(boolean activeFl) {
		this.activeFl = activeFl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	
	@Override
	public String toString() {
		return "user [id=" + id + ", name=" + name + ", email=" + email + ", role=" + role + ", password=" + password
				+ ", activeFl=" + activeFl + ", imageUrl=" + imageUrl + ", about=" + about + "]";
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
