package com.url.URL_Shortner.Controller;

import com.url.URL_Shortner.DTO.RegisterUserRequest;
import com.url.URL_Shortner.DTO.UserResponse;
import com.url.URL_Shortner.Entity.Role;
import com.url.URL_Shortner.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest registerUserRequest) {
        registerUserRequest.setRole(Role.USER);
        UserResponse userResponse = userService.registerUser(registerUserRequest);
        log.info("User registered: {}", registerUserRequest.getEmail());
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<UserResponse> registerByAdmin(
            @Valid @RequestBody RegisterUserRequest registerUserRequest) {
        UserResponse userResponse = userService.registerUser(registerUserRequest);
        log.info("Admin created user: {}", registerUserRequest.getEmail());
        return ResponseEntity.ok(userResponse);
    }
}