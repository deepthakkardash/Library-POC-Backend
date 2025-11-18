package com.example.libraryManagement.in.controller;

import com.example.libraryManagement.in.entites.Notification;
import com.example.libraryManagement.in.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController
{
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public Notification sentNotification(@RequestBody Notification notification)
    {
        return notificationService.sendNotification(notification);
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable int userId)
    {
        return notificationService.getUserNotifications(userId);
    }
}
