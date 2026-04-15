package com.url.URL_Shortner.Controller;

import com.url.URL_Shortner.Entity.Users;
import com.url.URL_Shortner.Repository.UserDetailsRepository;
import com.url.URL_Shortner.Utils.JWTUtil;
import com.url.URL_Shortner.sse.SseManager;

import com.url.URL_Shortner.sse.SseManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@AllArgsConstructor
public class SseController {

    private final SseManager sseManager;
    private final JWTUtil jwtUtil;
    private final UserDetailsRepository userDetailsRepository;

    @GetMapping("/sse/connect")
    public SseEmitter connect(@RequestParam("token") String token) {

        String email = jwtUtil.extractUsername(token);
        Users user = userDetailsRepository.findByEmail(email).orElseThrow();

        return sseManager.addEmitter(user.getId());
    }
}