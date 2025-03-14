package com.bank.user_service.User.dtos;


import java.util.List;

public class UserDetailsDto {
    private String username;
    private List<String> roles;

    // Constructors
    public UserDetailsDto() {}

    public UserDetailsDto(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

