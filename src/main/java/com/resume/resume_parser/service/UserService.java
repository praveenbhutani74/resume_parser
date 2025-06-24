package com.resume.resume_parser.service;

import com.resume.resume_parser.dto.UserDTO;

public interface UserService {
    UserDTO register(UserDTO userDTO);
    
    UserDTO login(UserDTO userDTO);
    
    
}
