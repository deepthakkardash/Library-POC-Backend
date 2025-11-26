package com.example.libraryManagement.in.config;

import com.example.libraryManagement.in.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                try {
                    String username = jwtTokenUtil.extractUsername(token);

                    int userId = jwtTokenUtil.extractUserId(token);

                    // SAVE TO SESSION
                    accessor.getSessionAttributes().put("userId", userId);

                    System.out.println("🌟 WebSocket CONNECT Authenticated userId = " + userId);

                } catch (Exception e) {
                    System.out.println("❌ Invalid WebSocket Token: " + e.getMessage());
                }
            } else {
                System.out.println("❌ No Authorization header in CONNECT frame");
            }
        }

        return message;
    }
}
