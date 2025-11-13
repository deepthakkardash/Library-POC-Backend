package com.example.libraryManagement.in.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.example.libraryManagement.in.entites.Notification;
import com.example.libraryManagement.in.entites.NotificationStatus;
import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.repository.userRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.libraryManagement.in.entites.Book;
import com.example.libraryManagement.in.repository.bookRepository;


@Slf4j
@Service
public class BookService {
	
	Scanner sc= new Scanner(System.in);
	
	@Autowired
	private bookRepository bookRepo;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private NotificationService notificationService;

//    private final static Logger logger= (Logger) LoggerFactory.getLogger(BookService.class);
	
	public Boolean SaveBook(String title,String isbn,int copies,String author, String category, int userId, String filepath)
	{

        log.info("Comes To Book Service");
		Book books=bookRepo.findByIsbn(isbn);
		if (books != null) {
			return false;
		}
		
		Book book= new Book(title, isbn, copies,author,category,filepath);
		book.setCreatedBy((userId));
		bookRepo.save(book);

        log.info("Books Added");
        List<User> users=userRepository.findByUserType("User");


        for (User user:users)
        {

            log.info("User: "+user.getUserName());

            Notification notif=Notification.builder()
                    .userId(user.getUserId())
                    .type("NEW_BOOK_ARRIVAL")
                    .targetRole("User")
                    .title("New Book Added!")
                    .entityType("BOOK")
                    .message("message")
                    .payload("{\"bookId\":"+book.getBookId()+ "}")
                    .status(NotificationStatus.PENDING)
                    .build();

            notificationService.sendNotification(notif);
        }



		return true;
	}
	
	public List<Book> newBooks()
	{
		return bookRepo.findTop4ByOrderByCreatedOnDesc();
	}
	
	public Book SearchBookById(int id)
	{
		return bookRepo.findByBookId(id);
	}
	
	public List<Book> GetAllBooks()
	{
		return bookRepo.findAll();
	}
	
	public void DisplayAllBook()
	{
		List<Book> books=GetAllBooks();
		
		for (int i = 0; i < books.size(); i++) {
			System.out.println("Book: "+i);
			System.out.println("\nTitle: "+books.get(i).getTitle());
			System.out.println("\nISBN: "+books.get(i).getIsbn());
			System.out.println("\nCopies Available: "+books.get(i).getNumberOfCopies());
		}
	}
	
	public Optional<Book> GetBook(Integer id)
	{
		return bookRepo.findById(id);
	}
	
	public void DisplayBook()
	{
			Book book=SearchBookByTitle();
			System.out.println("\nTitle: "+book.getTitle());
			System.out.println("\nISBN: "+book.getIsbn());
			System.out.println("\nCopies Available: "+book.getNumberOfCopies());
	}
	
	public boolean DeleteBook(int id)
	{
		Optional<Book> book=bookRepo.findById(id);
		if (book.isEmpty()) 
		{
			return false;
		}
		bookRepo.delete(book.get());
		return true;
	}
	
	public Book SearchBookByTitle()
	{
		System.out.println("Enter Book Title: ");
		String title=sc.nextLine();
		return bookRepo.findByTitle(title);
	}
	
	public boolean EditBook(int id, String title, String isbn, int copies,int userID ,MultipartFile imagefile) {
	    Book book = bookRepo.findByBookId(id);
	    System.out.println();
	    System.out.println();
	    System.out.println();
	    System.out.println("User ID : "+userID);
	    
	    if (book != null) {
	        book.setTitle(title);
	        book.setIsbn(isbn);
	        book.setNumberOfCopies(copies);
	        book.setEditedBy(userID);
	        book.setEditedOn(LocalDateTime.now());

	        // Handle image file update
	        if (imagefile != null && !imagefile.isEmpty()) {
	            // Delete old file if it exists
	            String oldImagePath = book.getImagePath();
	            if (oldImagePath != null) {
	                try {
	                    Path oldPath = Paths.get("").toAbsolutePath().resolve(oldImagePath.replaceFirst("/", ""));
	                    Files.deleteIfExists(oldPath);
	                } catch (Exception e) { e.printStackTrace(); }
	            }
	            // Save new file
	            try {
	                String uploadDir = "uploads/books/";
	                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
	                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

	                String extension = imagefile.getOriginalFilename()
	                        .substring(imagefile.getOriginalFilename().lastIndexOf("."));
	                String fileName = "Book_ISBN_" + isbn + extension;
	                Path filePath = uploadPath.resolve(fileName);
	                imagefile.transferTo(filePath.toFile());
	                book.setImagePath("/uploads/books/" + fileName);
	            } catch (Exception ex) { ex.printStackTrace(); }
	        }

	        bookRepo.save(book);
	        return true;
	    }
	    return false;
	}	
	
	public Book GetBook()
	{
		System.out.println("Enter ISBN: ");
		String isbn=sc.next();
		return bookRepo.findByIsbn(isbn);
	}
	
//	public Book BorrowBook()
//	{
		
//		Book book=availableBook();
//		if (book==null) {
//			return null;
//		}
//		book.setNumberOfCopies(book.getNumberOfCopies()-1);
//		return book;
//	}
	
	public void DisplayBook(Book book)
	{
		System.out.println("\nTitle: "+book.getTitle());
		System.out.println("\nISBN: "+book.getIsbn());
	}
	
	public boolean checkBookQuantity(Book book)
	{
		if (book.getNumberOfCopies()>0) {
			return true;
		}
		return false;
	}

	public void BorrowupdateQuantity(Book book)
	{
		book.setNumberOfCopies(book.getNumberOfCopies()-1);
		bookRepo.save(book);
	}
	
	public void ReturnupdateQuantity(Book book)
	{
		book.setNumberOfCopies(book.getNumberOfCopies()+1);
		bookRepo.save(book);
	}

	
//	public Book FindBookByISBN()
//	{}
	
	
//	public Book ReturnBook()
//	{}
	
	
}
