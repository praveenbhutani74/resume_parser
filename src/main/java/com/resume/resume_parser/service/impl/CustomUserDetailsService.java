package com.resume.resume_parser.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.resume.resume_parser.Entity.User;
import com.resume.resume_parser.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired 
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email)
		        .orElseThrow(() -> new UsernameNotFoundException("User with email not found: " + email));
				
				
		return new org.springframework.security.core.userdetails.User(
		        user.getEmail(),
		        user.getPassword(),
		        Collections.emptyList() 
		    );
	}


	
	
}
