package com.example.cloudnote.dto;

import com.example.cloudnote.model.User;

public class UserDTO {
    private Long id;
    private String username;
    private String email;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    // Getters


    public Long getId() {
        return id;
    }



    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}

