//package com.example.libraryManagement.in.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//
//
//@Configuration
//public class SecurityConfig {
//
//	
//	@Bean
//    public SecurityFilterChain  filterChain(HttpSecurity http) throws Exception {
//        http
//          .csrf().disable()
//          .authorizeRequests()
//            .antMatchers("/api/borrow/bookborrow").permitAll()  // Allow borrow endpoint without auth
//            .anyRequest().authenticated()
//          .and()
//          .formLogin(); // Optional: enable form login for other URLs
//
//        return http.build();
//    }
//}
