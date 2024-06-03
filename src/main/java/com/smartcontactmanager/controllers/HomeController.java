package com.smartcontactmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactmanager.repository.UserRepository;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping("/")
	public String connect(Model model) {
		model.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("title","SignUp - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping("/signin")
	public String signIn(Model model) {
		model.addAttribute("title","SignIn - Smart Contact Manager");
		return "signin";
	}
	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user,@RequestParam(value="agree", defaultValue = "false")boolean agree,Model model,
			HttpSession session)
	{
		try {
			
			if(!agree)
			{
				System.out.println("User has not agreed Terms and Conditions");
				throw new Exception("Please check out the terms and conditions!!");
			}
			
			System.out.println(user);
			user.setActiveFl(true);
			user.setRole("USER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User save = this.userRepository.save(user);
			
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered!!","alert-success"));
			
			return "signup";
			
		} catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went Wrong!!"+e.getMessage(),"alert-danger"));
			
			return "signup";
			
		}
		
		
	}

}
