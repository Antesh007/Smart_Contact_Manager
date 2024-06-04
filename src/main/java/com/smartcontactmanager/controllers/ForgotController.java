package com.smartcontactmanager.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.Message;
import com.smartcontactmanager.repository.UserRepository;
import com.smartcontactmanager.service.MessageService;

import jakarta.servlet.http.HttpSession;

import java.util.*;

@Controller
public class ForgotController {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	Random random = new Random(1000);
	
	@RequestMapping("/forgot")
	public String forgotPassword(Model model) {
		
		model.addAttribute("title", "Forgot Password");
		return "forgot-password-form";
	}
	
	@PostMapping("/send_otp")
	public String sendOTP(@RequestParam("email") String email,Model model, HttpSession session)
	{
		int otp = random.nextInt(999999);
		
		System.out.println(otp);
		
		String subject = "OTP from Smart Contact Manager";
		String body = "Please enter the following code on the page where you dropped for Verify OTP: "+otp;
		
		try {
		this.messageService.sendEmail(email, subject, body);
		session.setAttribute("sendOtp", otp);
		session.setAttribute("sendtoEmail", email);
		
		} catch(MailException  e) {
			session.setAttribute("message",new Message("Problem in sending Mail, Please check your Email Id", "alert-danger"));
			e.printStackTrace();
			return "forgot-password-form";
		}
		session.setAttribute("message",new Message("OTP send successfully !!!", "alert-success"));
		model.addAttribute("title","Verify OTP");
		return "verify_otp";
	}
	
	@PostMapping("/verify-otp")
	public String validateOTP(@RequestParam("otp") int userOTP,Model model, HttpSession session) {
		
		int sendOTP = (int) session.getAttribute("sendOtp");
		String email = (String) session.getAttribute("sendtoEmail");
		
		User user = this.userRepository.getUserByUserName(email);
		
		if(sendOTP == userOTP)
		{
				if(user == null) {
					session.setAttribute("message",new Message("User does not exists with this Email Id", "alert-warning"));
					return "forgot-password-form";
				}else {
					model.addAttribute("title","Change your Password");
					session.setAttribute("email", user.getEmail());
					return "change-password-form";
			}
		} else {
			session.setAttribute("message",new Message("Wrong OTP entered ! Please enter the correct OTP", "alert-danger"));
			return "verify_otp";
		}
		
	}
	

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String newPassword, Principal principal, HttpSession session)
	{
		String email = (String) session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		
			user.setPassword(this.passwordEncoder.encode(newPassword));
			this.userRepository.save(user);
			
			session.setAttribute("message",new Message("Password changed successfully !!!", "success"));
			return "signin";
	}

}
