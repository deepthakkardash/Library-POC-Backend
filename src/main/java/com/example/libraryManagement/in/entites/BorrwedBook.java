package com.example.libraryManagement.in.entites;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "borrowedbooks")
@Data
@NoArgsConstructor
public class BorrwedBook {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int borrowID;
	
	@ManyToOne
	@JoinColumn(name = "userId",nullable = false)
	@JsonBackReference
	private User user;
	
	@ManyToOne
	@JoinColumn(name="bookId", nullable = false)
	private Book book;
	
	
	@Column(nullable = false,updatable = false)
	@CreationTimestamp
	private LocalDateTime issueDate;
	
	@Column(nullable = true)
	private LocalDateTime returnDate;
	
	@Column(nullable = true)
	private LocalDateTime dueDate;
	
	@Column
	@CreationTimestamp
	private LocalDateTime createdOn;
	
	
	@Column
    private Integer createdBy;
	
	@Column
    private Integer editedBy;

    @Column
    private LocalDateTime editedOn;
	
	@PrePersist
	public void onCreate() {
		if (issueDate==null) {
			issueDate=LocalDateTime.now();
		}
		dueDate=issueDate.plusDays(15);
	}
	
	@Column
	private int fineAmount=0;
	
	
	public BorrwedBook(User user,Book book) {
		// TODO Auto-generated constructor stub
		this.user=user;
		this.book=book;
	}

	
	
}
