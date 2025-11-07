package com.example.libraryManagement.in.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.libraryManagement.in.entites.Book;
import com.example.libraryManagement.in.entites.BorrwedBook;
import com.example.libraryManagement.in.entites.User;


@Repository
public interface borrowedBookRepository extends JpaRepository<BorrwedBook, Integer>{
//		List<BorrwedBook> findByUser(User user);
		
//		List<BorrwedBook> findByUser_UserId(int userId); 
		BorrwedBook findByUserAndBook(User user, Book book);
		BorrwedBook findByUser_UserIdAndBook_BookIdAndReturnDateIsNull(int userId, int bookId);
		
		List<BorrwedBook> findByUserIdOrderByDueDateDesc(int userId);
		
		
		
		@Query(value = "SELECT b.* FROM books b " +
	               "JOIN borrowedbooks bb ON b.book_id = bb.book_id " +
	               "GROUP BY b.book_id " +
	               "ORDER BY COUNT(bb.book_id) DESC " +
	               "LIMIT 4", nativeQuery = true)
			List<Book> findTopBorrowedBooks();
		
}
