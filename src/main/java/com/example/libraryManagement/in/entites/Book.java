package com.example.libraryManagement.in.entites;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "books")
@EntityListeners(AuditingEntityListener.class)
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bookId;
	
	@Column
	private String title;
	
	@Column
	private String isbn;
	
	@Column
	private int NumberOfCopies;
	
	@Column
	private int totalCopies;
	

	@Column
	private String author;
	
	@Column
	@CreationTimestamp
	private LocalDateTime createdOn;
	
	@Column
	private String category;
	
	
	@Column
	private String imagePath;	
	
	
		@Column
	    private Integer createdBy;

//	    @Column(updatable = false)
//	    @CreationTimestamp
//	    private LocalDateTime createdTime;

	    @Column
	    private Integer editedBy;
	    
	    
	    @Column
	    private LocalDateTime editedOn;
		    
	    public Book(String title, String isbn, int NumberOfCopies,String author,String category, String filepath) 
	    {	
			this.isbn=isbn;
			this.title=title;
			this.NumberOfCopies=NumberOfCopies;
			this.author=author;
			totalCopies=NumberOfCopies;
			this.category=category;
			imagePath=filepath;
			// TODO Auto-generated constructor stub
	    }
	
}
