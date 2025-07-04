package com.resume.resume_parser.controller;

import com.resume.resume_parser.dto.UserDTO;
import com.resume.resume_parser.dto.UserDTORequest;
import com.resume.resume_parser.dto.UserLoginRequest;
import com.resume.resume_parser.response.Response;
import com.resume.resume_parser.service.UserService;
import com.resume.resume_parser.util.JWTUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "User APIs", description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;


    
    @PostMapping("/register")
    public Response<UserDTO> register(@RequestBody UserDTORequest userDTO) {
    	Response<UserDTO> response = new Response<>();
    	response.setPayload(userService.register(userDTO));
    	response.setSuccess(true);
    	response.setMessage("User registered successfully");
       return response;
    }

    @PostMapping("/login")
    public Response<UserDTO> login(@RequestBody UserLoginRequest loginRequest) {
    	Response<UserDTO> response = new Response<>();
    	response.setPayload(userService.login(loginRequest));
    	response.setSuccess(true);
    	response.setMessage("User login successfully");
       return response;
    }

    
}
