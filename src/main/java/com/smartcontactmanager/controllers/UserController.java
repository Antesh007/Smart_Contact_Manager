package com.smartcontactmanager.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/welcome")
	public String user_Dashboard(Model model,Principal principal)
	{
		String emailString = principal.getName();
		System.out.println("Username"+emailString);
		
		User userByUserName = this.userRepository.getUserByUserName(emailString);
		System.out.println("User Details"+userByUserName.toString());
		
		model.addAttribute("user",userByUserName);
		
		return "normal/user_dashboard";
	}

}
