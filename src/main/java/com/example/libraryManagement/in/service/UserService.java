package com.example.libraryManagement.in.service;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.repository.userRepository;

@Service
public class UserService {
	
	Scanner sc=new Scanner(System.in);
	
	@Autowired
	private userRepository userRepo;
	
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
		
//		User user=userRepo.findByUserName(username);
//		
//		if (user!=null) {
//			return true;
//		}
//		return false;
	}
	
	public User FindByUserId(int id)
	{
		return userRepo.findByUserId(id);
	}
	
	
	public User LoginUser(String username,String password, String usertype)
	{

		    User user= userRepo.findByUserName(username);
		
		    if (user != null) {
			if(user.getPassword().equals(password) && user.getUserType().equals(usertype))
			{
				return user;
			}
			//			return user.getPassword().equals(password);
		}
		return null;
//		User u=VerifyUser();
//		
//		if (u == null) {
//			return null;
//		}
//		LoggedUser=u;
//		System.out.println("Logged Successfully");
//		return u.getUserType();
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
