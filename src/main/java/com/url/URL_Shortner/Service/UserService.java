package com.url.URL_Shortner.Service;

import com.url.URL_Shortner.DTO.RegisterUserRequest;
import com.url.URL_Shortner.DTO.UserResponse;
import com.url.URL_Shortner.Entity.Users;
import com.url.URL_Shortner.Repository.UserDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(RegisterUserRequest registerUserRequest) {
        if (userDetailsRepository.findByEmail(registerUserRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + registerUserRequest.getEmail());
        }

        Users users = new Users();
        users.setEmail(registerUserRequest.getEmail());
        users.setRole(registerUserRequest.getRole());
        users.setUsername(registerUserRequest.getUsername());
        users.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));

        Users savedUser = userDetailsRepository.save(users);
        log.info("New user registered with email: {}", savedUser.getEmail());

        return new UserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getRole().name());
    }

    public Users findByEmail(String email) {
        return userDetailsRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email)
        );
    }
}