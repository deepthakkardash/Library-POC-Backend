package com.example.libraryManagement.in.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final Logger logger= LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractJwtToken(request);

        // ✅ Check if token is null, blank, or "undefined"
        if (token == null || token.isBlank() || token.equalsIgnoreCase("undefined")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Additional safety: check after generation/extraction
        if (!StringUtils.hasText(token)) {
            logger.error("⚠️ JWT token found but is blank or invalid. token: {}",token);
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;

        try {
            username = jwtTokenUtil.extractUsername(token);
        } catch (Exception e) {
            logger.error("⚠️ Invalid or expired JWT token: {}", token);
            filterChain.doFilter(request, response);
            return;
        }

        // Continue only if username is valid and not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // ✅ Ensure token is valid for the extracted username
            if (jwtTokenUtil.validateToken(token, username)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username, null, new ArrayList<>());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.error("⚠️ Token validation failed for username: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from the Authorization header or cookies.
     */
    private String extractJwtToken(HttpServletRequest request) {
        // 1️⃣ Try Authorization header
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 2️⃣ Try from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("Authorization".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 3️⃣ No token found
        return null;
    }
}