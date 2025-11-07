package com.example.libraryManagement.in.dto;

import lombok.Data;

@Data
public class BookRequest {

	private String title;
	private String isbn;
	private int numberOfCopies;
	private String author;
	private String category;
	private int userId;
}
