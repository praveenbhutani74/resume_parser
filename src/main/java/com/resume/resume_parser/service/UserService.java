package com.resume.resume_parser.service;

import com.resume.resume_parser.dto.UserDTO;
import com.resume.resume_parser.dto.UserDTORequest;
import com.resume.resume_parser.dto.UserLoginRequest;

public interface UserService {
    UserDTO register(UserDTORequest userDTO);
    
    UserDTO login(UserLoginRequest userDTO);
    
    
}
