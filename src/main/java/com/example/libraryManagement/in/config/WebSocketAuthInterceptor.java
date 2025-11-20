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

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // Get the raw URI from handshake
            String rawUri = (String) accessor.getSessionAttributes()
                    .get("javax.servlet.http.HttpServletRequest.request_uri");

            if (rawUri == null) {
                rawUri = (String) accessor.getSessionAttributes()
                        .get("org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor.URI");
            }

            if (rawUri == null) {
                System.out.println("❌ No handshake URI found");
                return message;
            }

            // Example rawUri:
            // /ws/123/xyz/websocket?token=Bearer abc.def.ghi
            if (rawUri.contains("token=")) {
                String token = rawUri.substring(rawUri.indexOf("token=") + 6);

                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                try {
                    String username = jwtTokenUtil.extractUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    accessor.setUser(auth);
                    System.out.println("✅ WebSocket authenticated user: " + username);
                } catch (Exception e) {
                    System.out.println("❌ Invalid JWT in WebSocket: " + e.getMessage());
                }
            } else {
                System.out.println("❌ No token found in WebSocket URL");
            }
        }

        return message;
    }
}
