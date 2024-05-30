package com.smartcontactmanager.controllers;



import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ssl.SslProperties.Bundles.Watch.File;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.repository.UserRepository;

import jakarta.persistence.criteria.Path;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute
	public void addModelDetails(Model model, Principal principal) {
		
		String emailString = principal.getName();
		System.out.println("Username"+emailString);
		
		User userByUserName = this.userRepository.getUserByUserName(emailString);
		System.out.println("User Details"+userByUserName.toString());
		
		model.addAttribute("user",userByUserName);
	}
	
	@RequestMapping("/welcome")
	public String user_Dashboard(Model model,Principal principal)
	{
		model.addAttribute("title","User DashBoard");
		return "normal/user_dashboard";
	}
	
	@GetMapping("/add-contact")
	public String addContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
	@PostMapping("/process-contact")
	public String processContactForm(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal principal)
	{
		try {
		String userNameString = principal.getName();
		User user = this.userRepository.getUserByUserName(userNameString);
		
		contact.setUser(user);
		if (file.isEmpty()) {
			System.out.println("File is Empty");
		} else {
			contact.setImage(file.getOriginalFilename());
			
			String saveFiles = new ClassPathResource("static/image").getFile().getAbsolutePath();
			
			java.nio.file.Path path =  Paths.get(saveFiles , file.getOriginalFilename());
			try {
			//	OutputStream in = new ByteArrayInputStream(file.getBytes());
			file.transferTo(path);
			} catch (Exception e) {
				System.out.println("Ërror in file Uploading" +e);
			}
		}
		user.getContacts().add(contact);
		
		this.userRepository.save(user);
		System.out.println("Contact successfully saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "normal/add_contact_form";
	}

}
