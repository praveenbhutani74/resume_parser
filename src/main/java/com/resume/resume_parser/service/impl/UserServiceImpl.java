package com.resume.resume_parser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.resume.resume_parser.Entity.User;
import com.resume.resume_parser.dto.UserDTO;
import com.resume.resume_parser.exception.EmailAlreadyExistsException;
import com.resume.resume_parser.repository.UserRepository;
import com.resume.resume_parser.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	@Override
	public UserDTO register(UserDTO userDTO) {
		userRepository.findByEmail(userDTO.getEmail())
	    .ifPresent(user -> {
	        throw new EmailAlreadyExistsException("Email already registered");
	    });
		
		User newUser  = new User();
		newUser.setEmail(userDTO.getEmail());
		newUser.setUserName(userDTO.getUserName());
		newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
	    userRepository.save(newUser );
	    
	    UserDTO responseDTO = new UserDTO();
	    responseDTO.setEmail(newUser.getEmail());
	    responseDTO.setPassword(null); 
	    responseDTO.setUserName(newUser.getUserName());

	    return responseDTO;
	}
	
	

}
