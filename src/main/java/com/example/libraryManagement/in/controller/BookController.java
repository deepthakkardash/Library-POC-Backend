package com.example.libraryManagement.in.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.example.libraryManagement.in.config.JwtTokenUtil;
import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.service.JwtService;
import com.example.libraryManagement.in.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.libraryManagement.in.dto.ApiResponse;
import com.example.libraryManagement.in.dto.BookRequest;
import com.example.libraryManagement.in.entites.Book;
import com.example.libraryManagement.in.service.BookService;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:5173")
public class BookController {
	@Autowired
	private BookService bookService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

	private static final Logger logger=LoggerFactory.getLogger(BookController.class);

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
	    List<Book> books = bookService.GetAllBooks();

	    ApiResponse<List<Book>> res = new ApiResponse<>(
	        "success",
	        "Books fetched successfully",
	        books
	    );
	    logger.info("returning all books data");
	    return ResponseEntity.ok(res);
	}
	
	@GetMapping("/newlyadded")
	public ResponseEntity<ApiResponse<List<Book>>> newBooks(){
		List<Book> newbooks=bookService.newBooks();
		
		ApiResponse<List<Book>> res=new ApiResponse<List<Book>>("success", "Books fetched successfully", newbooks);
	    logger.info("returning all newly added books data");
		return ResponseEntity.ok(res);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Book>> getBook(@PathVariable int id) throws Exception
	{
		Book book=bookService.SearchBookById(id);
		
		if (book!=null) {
			ApiResponse<Book> res=new ApiResponse<Book>("success", "Get Successfully", book);
		    logger.info("returning books data");
			return ResponseEntity.ok(res);  
		}
		

	    logger.info("Failed To Fetch books data");
//		ApiResponse<Book> res = new ApiResponse<>("error", "Book Not Found", null);
	    throw new Exception();
	}
	
	
	@PutMapping(value = "/edit/{id}", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> editBook(
	    @PathVariable int id,
	    @RequestPart("book") BookRequest bookRequest,
	    @RequestPart(value = "image", required = false) MultipartFile imagefile) {

        User user=jwtService.getAuthenticatedUser();
		
		logger.info("username :::: "+user.getUserId());
		
	    boolean updated = bookService.EditBook(
	        id,
	        bookRequest.getTitle(),
	        bookRequest.getIsbn(),
	        bookRequest.getNumberOfCopies(),
	        user.getUserId(),
	        imagefile);

	    if (updated) {
	        ApiResponse<String> res = new ApiResponse<>("success", "Updated Successfully!!", null);

		    logger.info("Book Data Edited");
	        return ResponseEntity.ok(res);
	    }

	    ApiResponse<String> res = new ApiResponse<>("error", "Book Not Found", null);

	    logger.info("Failed To To Find Book");
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	}
	
	
	@PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> addBook(
	    @RequestPart("book") BookRequest bookrequest,
	    @RequestPart("image") MultipartFile imagefile) {

        User user=jwtService.getAuthenticatedUser();

	    String fileName = null;
	    try {
	        String uploadDir = "uploads/books/";
	        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }

	        String originalFilename = imagefile.getOriginalFilename();
	        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
	        fileName = "Book_ISBN_" + bookrequest.getIsbn() + extension;

	        Path filePath = uploadPath.resolve(fileName);
	        imagefile.transferTo(filePath.toFile());

	    } catch (Exception e) {
	        e.printStackTrace();
	        ApiResponse<String> res = new ApiResponse<>(
	            "error",
	            "Failed to upload image: " + e.getMessage(),
	            null
	        );
		    logger.info("Failed To Edit Data");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	    }

	    boolean added = bookService.SaveBook(
	        bookrequest.getTitle(),
	        bookrequest.getIsbn(),
	        bookrequest.getNumberOfCopies(),
	        bookrequest.getAuthor(),
	        bookrequest.getCategory(),
	        user.getUserId(),
	        "/uploads/books/" + fileName);

	    if (added) {
	        ApiResponse<String> res = new ApiResponse<>(
	            "success",
	            "Book added successfully",
	            null
	        );
	        logger.info("Book Data Edited");
	        return ResponseEntity.ok(res);
	    }

	    ApiResponse<String> res = new ApiResponse<>(
	        "error",
	        "Book Already Exists",
	        null
	    );
	    logger.info("Fail to Edit");
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable int id) {
	    Book book = bookService.SearchBookById(id);

	    if (book == null) {
	        ApiResponse<String> res = new ApiResponse<>("error", "Book Not Found", null);
	        logger.info("Book Not Found");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	    }

	    // Get photo path from the book object
	    String photoPath = book.getImagePath(); // e.g. "/uploads/books/Book_ISBN_1234567890.jpg"

	    // Build absolute path to photo file
	    Path absolutePhotoPath = Paths.get("").toAbsolutePath().resolve(photoPath.replaceFirst("/", ""));

	    boolean imageDeleted = false;
	    try {
	        imageDeleted = Files.deleteIfExists(absolutePhotoPath);
	    } catch (Exception e) {
	        e.printStackTrace(); // Log deletion error
	    }

	    // Delete book record from database (consider returning false if actual deletion fails)
	    boolean deleted = bookService.DeleteBook(id);

	    if (deleted) {
	        String msg = imageDeleted ? "Book and photo deleted successfully" : "Book deleted, but photo was missing";
	        logger.info("Book Deleted But Photo is Missing!!");
	        ApiResponse<String> res = new ApiResponse<>("success", msg, null);
	        return ResponseEntity.ok(res);
	    }
        else {
            System.out.println("deletion failed here ");
        }
	    ApiResponse<String> res = new ApiResponse<>("error", "Book Not Found", null);
	    logger.info("Book is Not Found!!");
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	}

}
