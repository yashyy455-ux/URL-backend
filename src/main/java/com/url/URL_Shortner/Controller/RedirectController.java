package com.url.URL_Shortner.Controller;


import com.url.URL_Shortner.Entity.UrlMapping;
import com.url.URL_Shortner.Service.UrlMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RedirectController {

    private UrlMappingService urlMappingService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        // Check cache first
        String originalUrl = urlMappingService.getCachedOriginalUrl(shortUrl);
        if (originalUrl != null) {
            // Still record the click
            urlMappingService.getOriginalUrl(shortUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", originalUrl);
            return ResponseEntity.status(302).headers(headers).build();
        }
        return ResponseEntity.notFound().build();
    }