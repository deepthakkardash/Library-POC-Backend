package com.example.libraryManagement.in.service;

import com.example.libraryManagement.in.entites.Notification;
import com.example.libraryManagement.in.entites.NotificationStatus;
import com.example.libraryManagement.in.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class NotificationService
{
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notification sendNotification(Notification notification)
    {
        log.info("Comes to Send Notification!!");
        notification.setStatus(NotificationStatus.PENDING);
        Notification saved=notificationRepository.save(notification);

        if (notification.getUserId() != null && notification.getUserId() != 0) {
            log.info("Comes in if with user ID : "+notification.getUserId());
            messagingTemplate.convertAndSendToUser(saved.getUserId().toString(),"/queue/notification" , saved);
        }

        // 2️⃣ Send role-based notifications
        if (notification.getTargetRole() != null) {

            // SPECIAL CASE — NEW BOOK ADDED (broadcast to all users)
            if ("NEW_BOOK_ARRIVAL".equals(notification.getType())) {
                messagingTemplate.convertAndSend("/topic/books", saved);
            } else {
                messagingTemplate.convertAndSend(
                        "/topic/" + notification.getTargetRole().toLowerCase(),
                        saved
                );
            }
        }

else if ((notification.getUserId() == null || notification.getUserId() == 0) && notification.getTargetRole() == null) {
        //else {
            messagingTemplate.convertAndSend("/topic/all", saved);
        }


        saved.setStatus(NotificationStatus.SENT);

        log.info("status sending...");
        return notificationRepository.save(saved);
    }

    public List<Notification> getUserNotifications(long userId)
    {
        return notificationRepository.findByUserId(userId);
    }

}
