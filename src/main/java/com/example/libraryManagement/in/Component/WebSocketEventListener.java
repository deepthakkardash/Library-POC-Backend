package com.example.libraryManagement.in.Component;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Slf4j
@Component
public class WebSocketEventListener {


    @Autowired
    private OnlineUserTracker onlineUserTracker;

    @EventListener
    public void handleWebSocketConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getSessionAttributes() == null) {
            log.warn("No session attributes found in CONNECT event");
            return;
        }

        String userId = (String) accessor.getSessionAttributes().get("userId");

        log.info("userID " + userId);

        if (userId != null) {
            onlineUserTracker.userConnected(Long.valueOf(userId));
            log.info("User connected: " + userId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getSessionAttributes() == null) {
            log.warn("No session attributes found in DISCONNECT event");
            return;
        }

        String userId = (String) accessor.getSessionAttributes().get("userId");

        if (userId != null) {
            onlineUserTracker.userDisconnected(Long.valueOf(userId));
            log.info("User disconnected: " + userId);
        }
    }



}
