package com.resume.resume_parser.controller;

import com.resume.resume_parser.dto.UserDTO;
import com.resume.resume_parser.response.Response;
import com.resume.resume_parser.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User APIs", description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    
    
    @PostMapping("/register")
    public Response<UserDTO> register(@RequestBody UserDTO userDTO) {
    	Response<UserDTO> response = new Response<>();
    	response.setPayload(userService.register(userDTO));
    	response.setSuccess(true);
    	response.setMessage("User registered successfully");
       return response;
    }


    
}
