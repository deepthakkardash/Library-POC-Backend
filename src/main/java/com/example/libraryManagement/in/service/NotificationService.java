package com.example.libraryManagement.in.service;

import com.example.libraryManagement.in.entites.Notification;
import com.example.libraryManagement.in.entites.NotificationStatus;
import com.example.libraryManagement.in.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NotificationService
{
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private JwtService jwtService;

    public Notification sendNotification(Notification notification)
    {
        log.info("Comes to Send Notification!!");
        notification.setStatus(NotificationStatus.PENDING);
        Notification saved=notificationRepository.save(notification);

        // 1️⃣ Private notification (specific user)
        if (notification.getUserId() != null && notification.getUserId() != 0) {
            messagingTemplate.convertAndSendToUser(
                    saved.getUserId().toString(),
                    "/queue/notification",
                    saved
            );
        }

        // 2️⃣ NEW BOOK ARRIVAL → ONLY for users → ONLY broadcast on /topic/books
        if ("NEW_BOOK_ARRIVAL".equals(notification.getType())) {
            messagingTemplate.convertAndSend("/topic/books", saved);
            return saved;
        }

        // 3️⃣ BOOK BORROWED → for admins → broadcast on /topic/admin
        if ("BOOK_BORROWED".equals(notification.getType())) {
            messagingTemplate.convertAndSend("/topic/admin", saved);
            return saved;
        }

        // 4️⃣ Role-based notifications ONLY if needed
        if (notification.getTargetRole() != null) {
            messagingTemplate.convertAndSend(
                    "/topic/" + notification.getTargetRole().toLowerCase(),
                    saved
            );
            return saved;
        }


        saved.setStatus(NotificationStatus.SENT);

        log.info("status sending...");
        return notificationRepository.save(saved);
    }

    public List<Notification> getUserNotifications(long userId)
    {
        return notificationRepository.findByUserId(userId);
    }


    public List<Notification> getUnreadNotifications() {
        int userId=jwtService.getAuthenticatedUser().getUserId();
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public Notification markAsRead(Long notificationId) {

        log.info("comes in service");
        Notification notif = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notif.setIsRead(true);
        notif.setReadAt(LocalDateTime.now());

        return notificationRepository.save(notif);
    }
}
