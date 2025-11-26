package com.example.libraryManagement.in.controller;

import com.example.libraryManagement.in.config.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.libraryManagement.in.dto.ApiResponse;
import com.example.libraryManagement.in.dto.LoginRequest;
import com.example.libraryManagement.in.dto.UserRequest;
import com.example.libraryManagement.in.dto.UserResponse;
import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.service.UserService;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
	
	private static final Logger logger=LoggerFactory.getLogger(AuthController.class); 
	
	@Autowired
	private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) throws Exception {

        logger.info("Started Login Controller");

        ApiResponse<Map<String, Object>> loginResponse =
                userService.LoginUser(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getUsertype(), response);

        logger.info("Logged in Successfully!!");
        return ResponseEntity.ok(loginResponse);
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

//    @GetMapping("/token")
//    public ResponseEntity<String> getJwtToken(
//            @CookieValue(name = "Authorization", required = false) String token) {
//
//        if (token == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("No JWT cookie found");
//        }
//
//        return ResponseEntity.ok(token);
//    }
}