package com.example.libraryManagement.in.entites;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column
	private String userName;
	
	@Column
	private String password;
	
	@Column
	private String userType;
	
	@Column
	private int booksBorrowed=0;
	
	@Column
	@CreationTimestamp
	private LocalDateTime createdOn;
	
	
	@Column
    private Integer editedBy;

    @Column
    private LocalDateTime editedTime;


	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	@JsonBackReference
	private List<BorrwedBook> borroredBooks;

	public User() {}
	

	public User(String username,String password,String userType) {
		this.userName=username;
		this.password=password;
		this.userType=userType;
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {return userId;}
	public String getPassword() {return password;}
	public String getUsername() {return userName;}
	public String getUserType() {return userType;}
	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public int getBooksBorrowed() {
		return booksBorrowed;
	}


	public void setBooksBorrowed(int booksBorrowed) {
		this.booksBorrowed = booksBorrowed;
	}


	public List<BorrwedBook> getBorroredBooks() {
		return borroredBooks;
	}


	public void setBorroredBooks(List<BorrwedBook> borroredBooks) {
		this.borroredBooks = borroredBooks;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}


	public int getTotalBorrowed() {return booksBorrowed;}
	
	
	public void SetTotalBorrowed(int books) {booksBorrowed=books;}
}
