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

    /**
     * Fired AFTER WebSocket CONNECT, when session attributes are available.
     */
    @EventListener
    public void handleWebSocketConnect(SessionConnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) accessor.getSessionAttributes().get("userId");

//        String userId = accessor.getFirstNativeHeader("userId");

        log.info("CONNECT FRAME received | userId = {}", userId);

        if (userId != null) {
//            event.getMessage().getHeaders().put("userId", userId);
            onlineUserTracker.userConnected(userId);
        } else {
            log.warn("❌ userId not found in CONNECT headers");
        }
    }


    /**
     * Fired when a user disconnects from WebSocket.
     */
    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getSessionAttributes() == null) {
            log.warn("⚠ No session attributes found in DISCONNECT event");
            return;
        }

        String userId = (String) accessor.getSessionAttributes().get("userId");

        log.info("🔴 SessionDisconnectEvent received | userId = {}", userId);

        if (userId != null) {
            onlineUserTracker.userDisconnected(userId);
            log.info("🔻 User disconnected: {}", userId);
        } else {
            log.warn("⚠ userId is NULL in SessionDisconnectEvent");
        }
    }
}
