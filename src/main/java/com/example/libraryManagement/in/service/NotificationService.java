package com.example.libraryManagement.in.service;

import com.example.libraryManagement.in.entites.Notification;
import com.example.libraryManagement.in.entites.NotificationStatus;
import com.example.libraryManagement.in.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//    private  final static Logger logger= LoggerFactory.getLogger(NotificationService.class);



    public Notification sendNotification(Notification notification)
    {
        log.info("Comes to Send Notification!!");
        notification.setStatus(NotificationStatus.PENDING);
        Notification saved=notificationRepository.save(notification);

        if(notification.getUserId()!=0)
        {
          messagingTemplate.convertAndSend("/queue/user-"+notification.getUserId(),saved);
        } else if (notification.getTargetRole()!=null) {
            messagingTemplate.convertAndSend("/topic/"+notification.getTargetRole().toLowerCase(),saved);
        }
        else {
            messagingTemplate.convertAndSend("/topic/all",saved);
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
