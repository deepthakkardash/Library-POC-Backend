package com.example.libraryManagement.in.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notf_id;

    @Column(name = "userId", nullable = true)
    private Integer userId;

    @Column(nullable = false)
    private String targetRole;

    @Column(nullable = false,length = 50)
    private String type;

    @Column(nullable = false, length = 255)
    private String title;

    @Column
    private String message;

    @Builder.Default
    @Column(name = "is_read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isRead = false;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime readAt;

    @Column
    private String entityType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NotificationStatus status;
}
