package com.smartcontactmanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping("/welcome")
	public String user_Dashboard()
	{
		return "normal/user_dashboard";
	}

}
