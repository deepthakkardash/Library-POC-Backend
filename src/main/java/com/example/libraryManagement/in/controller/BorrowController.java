package com.example.libraryManagement.in.controller;

import java.util.List;
import com.example.libraryManagement.in.config.JwtTokenUtil;
import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.service.JwtService;
import com.example.libraryManagement.in.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.libraryManagement.in.dto.ApiResponse;
import com.example.libraryManagement.in.dto.BorrowRequest;
import com.example.libraryManagement.in.entites.Book;
import com.example.libraryManagement.in.entites.BorrwedBook;
import com.example.libraryManagement.in.service.BorrowedBookService;

@RestController
@RequestMapping("/api/borrow")
// @CrossOrigin(origins = "http://localhost:5173")
public class BorrowController {
	@Autowired
	private BorrowedBookService borrowService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtService jwtService;
	
	private static final Logger logger=LoggerFactory.getLogger(BorrowController.class);
	
	@PostMapping("/bookborrow")
	public ResponseEntity<ApiResponse<String>> borrowBook(
            @RequestBody BorrowRequest borrowRequest) {

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth == null || auth.getName() == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ApiResponse<>("error", "Unauthorized - no authentication found", null));
//        }
//
//        String username = auth.getName();
//        System.out.println("Logged in user: " + username);
//
//        // Fetch user
//        User user = userService.FindByUsername(username);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ApiResponse<>("error", "User not found", null));
//        }

        User user=jwtService.getAuthenticatedUser();

        boolean borrowed = borrowService.BorrowBook(borrowRequest.getBookId(), user.getUserId());

	    System.out.println("After boolean response");

	    if (borrowed) {
	        System.out.println("Borrowed successfully");

	        ApiResponse<String> res = new ApiResponse<>(
	            "success",
	            "Book Borrowed Successfully",
	            null
	        );
        	logger.info("Borrowed Successfully");
	        return ResponseEntity.ok(res);
	    }

	    System.out.println("Cannot borrow the book");

	    ApiResponse<String> res = new ApiResponse<>(
	        "error",
	        "Failed to borrow book",
	        null
	    );
	    logger.info("Failed to borrow book");
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<List<BorrwedBook>>> getAllBorrowed(@PathVariable int id) {
	    List<BorrwedBook> borrowedBooks = borrowService.MyBorrowedBooks(id);

	    ApiResponse<List<BorrwedBook>> res = new ApiResponse<>(
	        "success",
	        "Borrowed books fetched successfully",
	        borrowedBooks
	    );
    	logger.info("Borrowed books fetched successfully");
	    return ResponseEntity.ok(res);
	}
	
	
	
	@GetMapping("/mostBorrowed")
	public ResponseEntity<ApiResponse<List<Book>>> getMostBorrowedBooks()
	{
		List<Book> borrowedBooks=borrowService.getMostBorrowedBooks();
		
		ApiResponse<List<Book>> res=new ApiResponse<List<Book>>("success", "Fetched Successfully", borrowedBooks);
		logger.info("Most Borrowed Books Fetched Successfully");
		return ResponseEntity.ok(res);
	}

	
	
	@GetMapping("/history/{id}")
	public ResponseEntity<ApiResponse<List<BorrwedBook>>> getHistory(@PathVariable int id) {
	    List<BorrwedBook> historyList = borrowService.getAllHistory(id);

	    ApiResponse<List<BorrwedBook>> res = new ApiResponse<>(
	        "success",
	        "Borrow history fetched successfully",
	        historyList
	    );
	    logger.info("Borrow history fetched successfully");
	    return ResponseEntity.ok(res);
	}

	
	
	
	@GetMapping("/return")
	public ResponseEntity<ApiResponse<String>> returnBorrow(
	    @RequestParam int userId,
	    @RequestParam int bookId) {

	    boolean returned = borrowService.ReturnBorrowedBookByUserIdAndBookId(userId, bookId);

	    if (returned) {
	        ApiResponse<String> res = new ApiResponse<>(
	            "success",
	            "Book returned successfully",
	            null
	        );
	        logger.info("Book returned successfully");
	        return ResponseEntity.ok(res);
	    } else {
	        ApiResponse<String> res = new ApiResponse<>(
	            "error",
	            "Record not found",
	            null
	        );
	        logger.info("Record not found");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	    }
	}

}
