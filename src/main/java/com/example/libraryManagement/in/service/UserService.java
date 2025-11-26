package com.example.libraryManagement.in.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.example.libraryManagement.in.config.JwtTokenUtil;
import com.example.libraryManagement.in.dto.ApiResponse;
import com.example.libraryManagement.in.dto.UserResponse;
import exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.repository.userRepository;

@Service
public class UserService {
	
	Scanner sc=new Scanner(System.in);
	
	@Autowired
	private userRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	private User LoggedUser=null;


	public User getLoggedUser() {return LoggedUser;}
	
	
	public User RegisterUser(String username,String password,String userType)
	{
		
		User u1=new User();
		u1.setUserName(username);
		u1.setPassword(password);
		u1.setUserType(userType);
		
		
		return userRepo.save(u1);
	}

	public void DiplayUsers(User user)
	{
		System.out.println("\nUsername: "+user.getUserName());
		System.out.println("\nUser Type: "+user.getUserType());
		System.out.println("\nTotal Borrowed: "+user.getBooksBorrowed());
	}
	
	public List<User> DisplayAllUsers()
	{
		 return userRepo.findAll();
	}
	
	public User FindByUsername(String username)
	{
		
		return userRepo.findByUserName(username);

	}
	
	public User FindByUserId(int id)
	{
		return userRepo.findByUserId(id);
	}


    public ApiResponse<Map<String, Object>> LoginUser(String username, String password, String usertype, HttpServletResponse response) {
        User user = userRepo.findByUserName(username);

            if (user != null && user.getPassword().equals(password) && user.getUserType().equals(usertype)) {

            // ✅ Generate JWT token
            String token = jwtTokenUtil.generateToken(user.getUserName(),user.getId());

            // ✅ Create HttpOnly cookie for JWT
            ResponseCookie jwtCookie = ResponseCookie.from("Authorization", token)
                    .httpOnly(true)
                    .secure(false)          // set true in HTTPS
                    .path("/")
                    .maxAge(5 * 60 * 60)    // 5 hours
                    .sameSite("Lax")        // or "None" if frontend on different domain
                    .build();

            response.addHeader("Set-Cookie", jwtCookie.toString());

            // ✅ Prepare response data (same type as before)
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", new UserResponse(user.getUserId(), user.getUserName(), user.getUserType()));

            return new ApiResponse<>("success", "Login Successful!!", data);
        }

        throw new ResourceNotFoundException("Not Authorized");
    }

	
	public void LogOutUser() {
		LoggedUser=null;
	}
	
	
	public User VerifyUser()
	{
		System.out.println("Enter Username:");
		String username= sc.nextLine();
		
		User user=userRepo.findByUserName(username);
		
		if(user==null)
		{
			System.out.println("User Doesn't Exist");
			return null;
		}
		System.out.println("Enter Password: ");
		String password=sc.nextLine();
		System.out.println(password+"  "+user.getPassword());
		if ( user.getPassword().equals(password)) {
			return user;
		}
		System.out.println("Wrong Password");
		return null;
	}
	
	
	public void ReturnupdateQuantity()
	{
		LoggedUser.setBooksBorrowed(LoggedUser.getBooksBorrowed()-1);
		userRepo.save(LoggedUser);
	}
	
	public void BorrowupdateQuantity()
	{
		LoggedUser.setBooksBorrowed((LoggedUser.getBooksBorrowed()+1));
		userRepo.save(LoggedUser);
	}
}
