package com.example.libraryManagement.in.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.example.libraryManagement.in.entites.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.libraryManagement.in.repository.bookRepository;
import com.example.libraryManagement.in.repository.borrowedBookRepository;
import com.example.libraryManagement.in.repository.userRepository;


@Slf4j
@Service
public class BorrowedBookService {
	
	@Autowired
	private borrowedBookRepository borrowRepo;
	
	@Autowired
	private userRepository userRepo;
	
	@Autowired
	private bookRepository bookRepo;

    @Autowired
    private NotificationService notificationService;

	public void SaveBorrow(User u, Book b)
	{
        log.info("Comes to BorrowedBook Service");
        borrowRepo.save(new BorrwedBook(u, b));

        List<User> users=userRepo.findByUserType("Admin");

        for (User user:users)
        {
            log.info("Admin name : "+user.getUserName());

            Notification notif = Notification.builder()
                    .userId(user.getUserId())
                    .type("BOOK_BORROWED")
                    .targetRole("Admin")
                    .title("Book Borrowed!")
                    .entityType("BORROW")
                    .message("User '" + u.getUserName() + "' borrowed the book '" + b.getTitle() + "'.")
                    .payload("{\"bookId\":" + b.getBookId() + "}")
                    .status(NotificationStatus.PENDING)
                    .build();

            notificationService.sendNotification(notif);
        }
    }
	
	public void SaveReturn(List<BorrwedBook> borrowed)
	{
		borrowRepo.deleteAll(borrowed);
	}
	
	public boolean ReturnBorrowedBookByUserIdAndBookId(int userId, int bookId)
	{
		BorrwedBook borrowedbook = borrowRepo.findByUser_UserIdAndBook_BookIdAndReturnDateIsNull(userId, bookId);
		if(borrowedbook == null)
		{
			return false;
		}
		
		borrowedbook.getBook().setNumberOfCopies(borrowedbook.getBook().getTotalCopies()+1);
		
		borrowedbook.getUser().setBooksBorrowed(borrowedbook.getUser().getBooksBorrowed()-1);
		
		borrowedbook.setReturnDate(LocalDateTime.now());
		
		borrowedbook.setEditedBy(userId);
		
		borrowedbook.setEditedOn(LocalDateTime.now());
				
		borrowRepo.save(borrowedbook);
		return true;
		
//			User user=userRepo.findByUserId(userId);
//			Book book=bookRepo.findByBookId(bookId);
//			
//			user.setBooksBorrowed(user.getBooksBorrowed()-1);
//			userRepo.save(user);
//			
//			book.setNumberOfCopies(book.getNumberOfCopies()+1);
//			bookRepo.save(book);
//			(userRepo.getById(userId)).setBooksBorrowed(userRepo.getById(userId).getBooksBorrowed()+1);
//			(bookRepo.getById(bookId)).setNumberOfCopies(bookRepo.getById(bookId).getNumberOfCopies()-1);
//		}
//        return false;
	}
	
	public List<BorrwedBook> MyBorrowedBooks(int userId)
	{
		List<BorrwedBook>  borrowedlistdata=borrowRepo.findByUserIdOrderByDueDateDesc(userId);
		return borrowedlistdata.stream().filter(element->element.getReturnDate()==null).collect(Collectors.toList());
//		List<BorrwedBook> borrowed= borrowRepo.f		
//		return borrowed.stream().map(BorrwedBook::getBook).collect(Collectors.toList()); 
	}
	
	public List<BorrwedBook> getAllHistory(int userId)
	{
		return borrowRepo.findByUserIdOrderByDueDateDesc(userId);
	}
	
	public boolean BorrowBook(int bookId , int userId) {
	    System.out.println("Come in borrow service");

	    User user = userRepo.findByUserId(userId);
	    if (user == null) {
	        System.out.println("User not found for ID: " + userId);
	        return false;
	    }

	    Book book = bookRepo.findByBookId(bookId);
	    if (book == null) {
	        System.out.println("Book not found for ID: " + bookId);
	        return false;
	    }

	    BorrwedBook borrows = borrowRepo.findByUser_UserIdAndBook_BookIdAndReturnDateIsNull(user.getUserId(), book.getBookId());
	    if (borrows != null) {
	        System.out.println("Already exist");
	        return false;
	    }
	    
	    

	    System.out.println("this book user not already exist");
	    System.out.println("user and book bean getted");

	    if (book.getNumberOfCopies() <= 0) {
	        System.out.println("book is out of stock");
	        return false;	
	    }

	    user.setBooksBorrowed(user.getBooksBorrowed() + 1);
	    userRepo.save(user);

	    book.setNumberOfCopies(book.getNumberOfCopies() - 1);
	    bookRepo.save(book);
	    
	    BorrwedBook borrowedBook=new BorrwedBook(user, book);
	    borrowedBook.setCreatedBy(userId);
	    borrowRepo.save(borrowedBook);





        List<User> users=userRepo.findByUserType("Admin");

        for (User u:users)
        {
            log.info("Admin name : "+u.getUserName());

            Notification notif = Notification.builder()
                    .userId(u.getUserId())
                    .type("BOOK_BORROWED")
                    .targetRole("Admin")
                    .title("Book Borrowed!")
                    .entityType("BORROW")
                    .message("User '" + user.getUserName() + "' borrowed the book '" + book.getTitle() + "'.")
                    .payload("{\"bookId\":" + book.getBookId() + "}")
                    .status(NotificationStatus.PENDING)
                    .build();


            notificationService.sendNotification(notif);
        }
	    
	    System.out.println("user repo and book repo updated");
	    return true;
	}
	
	public List<Book> getMostBorrowedBooks()
	{
		return borrowRepo.findTopBorrowedBooks();
	}

}
