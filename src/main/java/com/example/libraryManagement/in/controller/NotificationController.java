package com.example.libraryManagement.in.controller;

import com.example.libraryManagement.in.entites.Notification;
import com.example.libraryManagement.in.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController
{
    @Autowired
    private NotificationService notificationService;

//    @PostMapping("/send")
//    public Notification sentNotification(@RequestBody Notification notification)
//    {
//        return notificationService.sendNotification(notification);
//    }

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable int userId)
    {
        return notificationService.getUserNotifications(userId);
    }



    @GetMapping("/user/unread")
    public List<Notification> getUnreadNotifications() {
        return notificationService.getUnreadNotifications();
    }

    // NEW: Mark notification as read
    @PutMapping("/read/{id}")
    public Notification markAsRead(@PathVariable Long id) {
        log.info("comes in controller");
        return notificationService.markAsRead(id);
    }

}
