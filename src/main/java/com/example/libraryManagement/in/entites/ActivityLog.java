package com.example.libraryManagement.in.entites;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ActivityLog {

	
	@Id @GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	

	@Column(nullable = false,length = 50)
	private String action;


	@Column(nullable = false,length = 50)
	private String entityType;
	
	
	@Column(name = "entity_id")
    private int entityId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    
    public ActivityLog(User user,String action,String entityType, int entityId,String description) 
    {
    	this.user=user;
    	this.action=action;
    	this.entityId=entityId;
    	this.entityType=entityType;
    	this.description=description;
    }

    
    
    
	
}
