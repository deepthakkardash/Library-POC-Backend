package com.example.libraryManagement.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.libraryManagement.in.entites.User;

@Repository
public interface userRepository extends JpaRepository<User, Integer>{
	User findByUserName(String userName);
	User findByUserId(int userId);
}
