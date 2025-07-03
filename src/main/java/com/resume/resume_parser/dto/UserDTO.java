package com.resume.resume_parser.dto;

public class UserDTO {
    private Integer id;
    private String userName;
    private String email;
    private String password;
    private String token;

    public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	// Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
