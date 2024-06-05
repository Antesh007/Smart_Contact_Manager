package com.smartcontactmanager.controllers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.UserEntity;
import com.smartcontactmanager.helper.Message;
import com.smartcontactmanager.repository.ContactRepository;
import com.smartcontactmanager.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	@ModelAttribute
	public void addModelDetails(Model model, Principal principal) {
		
		String emailString = principal.getName();
		System.out.println("Username"+emailString);
		
		UserEntity userByUserName = this.userRepository.getUserEntityByUserName(emailString);
		System.out.println("User Details"+userByUserName.toString());
		
		model.addAttribute("user",userByUserName);
	}
	
	@RequestMapping("/welcome")
	public String user_Dashboard(Model model,Principal principal,HttpSession session)
	{
		model.addAttribute("title","User DashBoard");
		session.removeAttribute("message");
		System.out.println("Entered");
		return "normal/user_dashboard";
	}
	
	@GetMapping("/add-contact")
	public String addContactForm(Model model,HttpSession session)
	{
		session.removeAttribute("message");
		model.addAttribute("title","Add Contact");
		model.addAttribute("header", "Add Contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
	@PostMapping("/process-contact/{contactId}")
	public String processContactForm(@ModelAttribute Contact contact,Model model,@RequestParam("profileImage") MultipartFile file,
			@PathVariable("contactId") Integer cId, Principal principal,final HttpSession session)
	{
		try {
		String userNameString = principal.getName();
		UserEntity user = this.userRepository.getUserEntityByUserName(userNameString);
		
		if (file.isEmpty()) {
			System.out.println("File is Empty");
			contact.setImage("contact.png");
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
				
		if(cId == 0) {
			
		contact.setUserEntity(user);
		user.getContacts().add(contact);
		this.userRepository.save(user);
		session.setAttribute("message",new Message("Contact saved successfully !!!", "success"));
		System.out.println("Contact successfully saved");
		
		} else {
			
			this.contactRepository.updateContactByCID(contact.getName(), contact.getSecondName(), contact.getWork(), contact.getEmail(), contact.getPhone(), contact.getImage(), contact.getDescription(), cId);
			session.setAttribute("message",new Message("Contact updated successfully !!!", "success"));
			System.out.println("Contact successfully Updated");
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message",new Message("Something Went Wrong !!!", "danger"));
		}
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
	@GetMapping("/show-contact/{page}")
	public String showContact(@PathVariable("page") Integer page, Model model,Principal principal,HttpSession session) {
		
		session.removeAttribute("message");
		model.addAttribute("title", "show contact");
		
		String userNameString = principal.getName();
		UserEntity user = this.userRepository.getUserEntityByUserName(userNameString);
		
		Pageable pageable = PageRequest.of(page, 7);
		
		Page<Contact> contactList = this.contactRepository.getContactByUserEntity(user.getId(), pageable);
		model.addAttribute("contact",contactList);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contactList.getTotalPages());
		
		return "normal/show_contact";
	}
	
	@GetMapping("/{contactId}/contact")
	public String showParticularContact(@PathVariable("contactId") Integer contactId, Model model, Principal principal,HttpSession session)
	{
		session.removeAttribute("message");
		Contact contact = this.contactRepository.findById(contactId).get();	
		String name = principal.getName();
		UserEntity user = this.userRepository.getUserEntityByUserName(name);
		
		if(user.getId() == contact.getUserEntity().getId())
		{
		model.addAttribute("title","User-"+contact.getName());
		model.addAttribute("singleContact",contact);
		} else {
			model.addAttribute("title","Don't Try to be OverSmart");
			return "login-fail";
		}
		return "normal/show_each_contact";
	}
	
	@GetMapping("/{contactId}/delete")
	public String deleteParticularContact(@PathVariable("contactId") Integer contactId, Model model, Principal principal, HttpSession session)
	{
		session.removeAttribute("message");
		String name = principal.getName();
		UserEntity user = this.userRepository.getUserEntityByUserName(name);
		
		Contact contact = this.contactRepository.findById(contactId).get();
		
		if(user.getId() == contact.getUserEntity().getId())
		{
			String imageString = this.contactRepository.getImageName(contactId);
				if(!imageString.equalsIgnoreCase("contact"))
				{
			try {
				String saveFiles = new ClassPathResource("static/image").getFile().getAbsolutePath();
				java.nio.file.Path path =  Paths.get(saveFiles , imageString);
				boolean deleteIfExists = Files.deleteIfExists(path);
				System.out.println("File successfully deleted"+deleteIfExists);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
			
		this.contactRepository.delete(contact);
		session.setAttribute("message",new Message("Contact deleted successfully !!!", "success"));
		System.out.println("Contact successfully Deleted"+contactId);
		} else {
			model.addAttribute("title","Don't Try to be OverSmart");
			return "login-fail";
		}
		
		return "redirect:/user/show-contact/0";
	}
	
	@GetMapping("/{contactId}/update")
	public String updateParticularContact(@PathVariable("contactId") Integer contactId, Model model, Principal principal,HttpSession session)
	{
		session.removeAttribute("message");
		String name = principal.getName();
		UserEntity user = this.userRepository.getUserEntityByUserName(name);
		
		Contact contact = this.contactRepository.findById(contactId).get();;
		
		if(user.getId() == contact.getUserEntity().getId())
		{
		model.addAttribute("contact", contact);
		model.addAttribute("title", "Update Contact");
		model.addAttribute("header", "Update Contact");
		} else {
			model.addAttribute("title","Don't Try to be OverSmart");
			return "login-fail";
		}
		return "normal/add_contact_form";
	}

	@GetMapping("/profile")
	public String yourProfile(Principal principal,Model model,HttpSession session)  {
		
		session.removeAttribute("message");
		String name = principal.getName();
		UserEntity user = this.userRepository.getUserEntityByUserName(name);
		
		model.addAttribute("user", user);
		model.addAttribute("title", "My Profile");
		
		return "normal/your_profile";
	}
	
	@GetMapping("/userSettings")
	public String userSettings(Model model,HttpSession session) {
		session.removeAttribute("message");
		model.addAttribute("title", "User Settings");
		return "normal/settings";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword, Principal principal, HttpSession session)
	{
		String name = principal.getName();
		UserEntity user = this.userRepository.getUserEntityByUserName(name);
		
		if(this.passwordEncoder.matches(oldPassword, user.getPassword()))	
		{
			user.setPassword(this.passwordEncoder.encode(newPassword));
			this.userRepository.save(user);
			session.setAttribute("message",new Message("Password changed successfully !!!", "success"));
			return "normal/user_dashboard";
		}
		session.setAttribute("message",new Message("Incorrect Old password !!!", "danger"));
	return "normal/settings";
	}
}
