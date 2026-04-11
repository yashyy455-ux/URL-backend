package com.url.URL_Shortner.Controller;

import com.url.URL_Shortner.DTO.AuthRequest;
import com.url.URL_Shortner.Utils.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> generateToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()));

            String token = jwtUtil.generateToken(authRequest.getEmail());
            log.info("Token generated for user: {}", authRequest.getEmail());
            return ResponseEntity.ok(Map.of("token", token));

        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for email: {}", authRequest.getEmail());
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }
    }
}