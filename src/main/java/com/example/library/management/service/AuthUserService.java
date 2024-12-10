package com.example.library.management.service;

import com.example.library.management.dto.auth.AuthRequest;
import com.example.library.management.model.User;
import com.example.library.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public AuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(AuthRequest authRequest) {
        // Check if username already exists
        Optional<User> existingUser = userRepository.findByUsername(authRequest.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Create new user
        User newUser = new User();
        newUser.setUsername(authRequest.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(authRequest.getPassword())); // Encode the password
        newUser.setRole("USER"); // Default role for new users

        // Save user to database
        return userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username
        Optional<User> userOptional = userRepository.findByUsername(username);

        // If user is not found, throw an exception
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOptional.get(); // Extract the user from Optional

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
