package com.example.libraryManagement.in.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.libraryManagement.in.dto.ApiResponse;
import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	@Autowired
	private UserService userService;
	

	private static final Logger logger=LoggerFactory.getLogger(UserController.class);
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
	    List<User> users = userService.DisplayAllUsers();

	    ApiResponse<List<User>> res = new ApiResponse<>(
	        "success",
	        "Users fetched successfully",
	        users
	    );
	    logger.info("Users fetched successfully");
	    return ResponseEntity.ok(res);
	}

	
	@GetMapping("/user/{username}")
	public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String username) {
	    User user = userService.FindByUsername(username);

	    if (user != null) {
	        user.setPassword(null);  // Ensure sensitive data is not exposed

	        ApiResponse<User> res = new ApiResponse<>(
	            "success",
	            "User fetched successfully",
	            user
	        );
	        logger.info("Users fetched successfully");
	        return ResponseEntity.ok(res);
	    }

	    ApiResponse<User> res = new ApiResponse<>(
	        "error",
	        "User not found",
	        null
	    );
	    logger.info("User not found");
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	}

	
	
	@GetMapping("/userid/{id}")
	public ResponseEntity<ApiResponse<User>> getUser(@PathVariable int id) {
	    User user = userService.FindByUserId(id);

	    if (user != null) {
	        user.setPassword(null);  // Ensure sensitive data is not exposed

	        ApiResponse<User> res = new ApiResponse<>(
	            "success",
	            "User fetched successfully",
	            user
	        );
	        logger.info("User fetched successfully");
	        return ResponseEntity.ok(res);
	    }

	    ApiResponse<User> res = new ApiResponse<>(
	        "error",
	        "User not found",
	        null
	    );

	    logger.info("User not found");
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	}

}
