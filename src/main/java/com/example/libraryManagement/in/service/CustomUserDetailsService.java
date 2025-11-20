package com.example.libraryManagement.in.service;

import com.example.libraryManagement.in.entites.User;
import com.example.libraryManagement.in.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private userRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByUserName(username);
        if (u == null) throw new UsernameNotFoundException("User not found");
        List<GrantedAuthority> auth = List.of(new SimpleGrantedAuthority("ROLE_" + u.getUserType().toUpperCase()));
        return new org.springframework.security.core.userdetails.User(u.getUserName(), u.getPassword(), auth);
    }
}
