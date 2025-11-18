package com.example.libraryManagement.in.service;


import com.example.libraryManagement.in.dto.ApiResponse;
import com.example.libraryManagement.in.entites.User;
import exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    public UserService userService;


    public User getAuthenticatedUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new UserNotFoundException("Token not created");
        }

        String username = auth.getName();
        System.out.println("Logged in user: " + username);

        // Fetch user
        User user = userService.FindByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User object not created");
        }

        return user;
    }



}
