package com.example.libraryManagement.in.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.libraryManagement.in.entites.Book;
import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.entites.BorrwedBook;

@Service
public class LibraryService {
	
	
	private final UserService userService;
	private final BookService bookService;
	private final BorrowedBookService borrowService;
	
	
	public LibraryService(UserService userService, BookService bookService, BorrowedBookService borrowService) {
		// TODO Auto-generated constructor stub
		this.userService=userService;
		this.bookService=bookService;
		this.borrowService=borrowService;
	}
	
	
	public void AddBook()
	{
//		bookService.SaveBook();
	}
	
	public void DispalyBooks()
	{
		bookService.DisplayAllBook();
	}
	
	public void DisplayAllUsers()
	{
		userService.DisplayAllUsers();
	}
	
	public void SearchBookByTitle()
	{
		bookService.DisplayBook();
	}
	
	public void DeleteBook()
	{
//		bookService.DeleteBook();
	}
	
	public void EditBook()
	{
//		bookService.EditBook();
	}
	
	public void MyBorrowedBooks()
	{
		User user=userService.getLoggedUser();
//		List<BorrwedBook> borrowed= borrowService.MyBorrowedBooks(user);
//		for (BorrwedBook borrwedBook : borrowed) {
//			bookService.DisplayBook(borrwedBook.getBook());
//		}
	}
	
	
	
//	public void FindBookTitle()
//	{
//	}

	
	
	
	public void AddUser()
	{
//		userService.RegisterUser();
	}
	
	public void LoginUser()
	{
//		return userService.LoginUser();
	}
	
	
	
	public void DisplayUser()
	{
//		userService.FindByUsername();
	}
	
	
	public void BorrowBook()
	{
			
		Book book= bookService.GetBook();
		User user=userService.getLoggedUser();
		
//		List<BorrwedBook> borrowed= borrowService.checkBorrow(book,user);
		
//		 if (borrowed != null && !borrowed.isEmpty()) {
//			System.out.println("You Have Already Borrowed This Books");
//			return;
//		}
		
		if(!bookService.checkBookQuantity(book))
		{			
			System.out.println("Sorry Book Is Out Of Stock");
			return;
		}
		
		
		userService.BorrowupdateQuantity();
		bookService.BorrowupdateQuantity(book);
		
		borrowService.SaveBorrow(user, book);
		System.out.println("Booked Borrowed Successfully!!");
	}
	
	public void ReturnBook()
	{
		Book book= bookService.GetBook();
		User user=userService.getLoggedUser();
	
//		List<BorrwedBook> borrowed= borrowService.checkBorrow(book,user);
		

		
		
//		if (borrowed == null || borrowed.isEmpty()) {
//			System.out.println("You Have Not Borrowed This Books");
//			return;
//		}
		
		
		userService.ReturnupdateQuantity();
		bookService.ReturnupdateQuantity(book);
		
//		borrowService.SaveReturn(borrowed);
		System.out.println("Book Return Successfully!!");
	}
	
	public void DisplayAllBook()
	{
		bookService.DisplayAllBook();
	}
	
//	public void DisplayBook(Integer id)
//	{
////		bookService.DisplayBook(id);
//	}
	
	
	public void LogOutUser()
	{
		userService.LogOutUser();
	}
	
//	public void BorrowBook()
//	{
//		userService.AddBook(null);
//	}
}
