package com.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontactmanager.repository.UserRepository;
import com.smartcontactmanager.user.User;

public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userByUserName = userRepository.getUserByUserName(username);
		
		if(userByUserName == null)
		{
			throw new UsernameNotFoundException("Could Not Found The User!!");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(userByUserName);
		
		return customUserDetails;
	}
	
	

}
