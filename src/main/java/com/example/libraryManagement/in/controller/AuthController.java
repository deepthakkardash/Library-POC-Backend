package com.example.libraryManagement.in.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.libraryManagement.in.dto.ApiResponse;
import com.example.libraryManagement.in.dto.LoginRequest;
import com.example.libraryManagement.in.dto.UserRequest;
import com.example.libraryManagement.in.dto.UserResponse;
import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.service.UserService;

import exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
	
	private static final Logger logger=LoggerFactory.getLogger(AuthController.class); 
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest loginRequest) throws Exception
	{
		System.out.println(loginRequest.getUsername());
        logger.info("Started Login Controller");
		User user=userService.LoginUser(loginRequest.getUsername(), loginRequest.getPassword(),loginRequest.getUsertype());
		if (user != null) 
		{
			UserResponse userResp=new UserResponse(user.getUserId(),user.getUserName(),user.getUserType());
			ApiResponse<UserResponse> response=new ApiResponse<UserResponse>("success","Login Successful!!", userResp);
//			System.out.println("Login Successfully");
			
//			User user,String action,String entityType, int entityId,String description
//			ActivityLog log=new ActivityLog(user, "Login", null, 0, "Login Successfully");
			
//			ApiResponse<UserResponse> response=new ApiResponse<UserResponse>();
			logger.info("Logged in Successfully!!");		
			return ResponseEntity.ok(response);
		}
		else 
		{
			System.out.println("not authorized");
			throw new ResourceNotFoundException("Not Authorized");
		}
	}
	
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<UserResponse>> signup(@RequestBody UserRequest signupRequest)
	{
		logger.info("Started Signup Controller");
		User user=userService.RegisterUser(signupRequest.getUsername(),signupRequest.getPassword(),signupRequest.getUsertype());
		if (user != null) 
		{
			UserResponse userResp=new UserResponse(user.getUserId(),user.getUserName(),user.getUserType());
			ApiResponse<UserResponse> response=new ApiResponse<UserResponse>("success","Register Successful!!", userResp);
			logger.info("Signup Successfully!!");
			return ResponseEntity.ok(response);
		}
		else {
			ApiResponse<UserResponse> response=new ApiResponse<UserResponse>("failed","Failed To Register", null);
			logger.info("Signup Failed!!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}