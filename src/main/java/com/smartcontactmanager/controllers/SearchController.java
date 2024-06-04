package com.smartcontactmanager.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.repository.ContactRepository;
import com.smartcontactmanager.repository.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/user/search/{query}")
	public ResponseEntity<?> showSearchResult(@PathVariable("query") String query, Principal principal) {
		
		User user = this.userRepository.getUserByUserName(principal.getName());
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
	}

}
