package com.example.libraryManagement.in.dto;

import lombok.Data;

@Data
public class BorrowRequest {
	private int bookId;
	private int userId;	
}
