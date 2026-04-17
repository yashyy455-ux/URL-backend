package com.url.URL_Shortner.Service;

import com.url.URL_Shortner.DTO.ClickEventDTO;
import com.url.URL_Shortner.DTO.UrlMappingDTO;
import com.url.URL_Shortner.Entity.ClickEvent;
import com.url.URL_Shortner.Entity.UrlMapping;
import com.url.URL_Shortner.Entity.Users;
import com.url.URL_Shortner.Event.UrlClickEvent;
import com.url.URL_Shortner.Repository.ClickEventRepository;
import com.url.URL_Shortner.Repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@Service
@AllArgsConstructor
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;
    private final ApplicationEventPublisher eventPublisher;


    @CacheEvict(value = "urls", allEntries = true)
    public UrlMappingDTO createShortUrl(String originalUrl, Users user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        log.info("Short URL created: {} for user: {}", shortUrl, user.getEmail());
        return convertToDto(savedUrlMapping);
    }

    @Cacheable(value = "urls", key = "#shortUrl")
    @Transactional
    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping == null) {
            log.warn("Short URL not found: {}", shortUrl);
            return null;
        }
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        urlMappingRepository.save(urlMapping);
        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setClickDate(LocalDateTime.now());
        clickEvent.setUrlMapping(urlMapping);
        clickEventRepository.save(clickEvent);
        eventPublisher.publishEvent(
                new UrlClickEvent(
                        urlMapping.getUser().getId(),
                        urlMapping.getShortUrl(),
                        urlMapping.getClickCount()
                )
        );
        return urlMapping;
    }

    public List<UrlMappingDTO> getUrlsByUser(Users user) {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        if (urlMapping == null) {
            log.warn("Short URL not found for analytics: {}", shortUrl);
            return Collections.emptyList();
        }

        return clickEventRepository
                .findByUrlMappingAndClickDateBetween(urlMapping, start, end)
                .stream()
                .collect(Collectors.groupingBy(
                        click -> click.getClickDate().toLocalDate(),
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    ClickEventDTO dto = new ClickEventDTO();
                    dto.setClickDate(entry.getKey());
                    dto.setCount(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(Users user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        return clickEventRepository
                .findByUrlMappingInAndClickDateBetween(
                        urlMappings,
                        start.atStartOfDay(),
                        end.plusDays(1).atStartOfDay())
                .stream()
                .collect(Collectors.groupingBy(
                        click -> click.getClickDate().toLocalDate(),
                        Collectors.counting()));
    }

    private UrlMappingDTO convertToDto(UrlMapping urlMapping) {
        UrlMappingDTO dto = new UrlMappingDTO();
        dto.setId(urlMapping.getId());
        dto.setOriginalUrl(urlMapping.getOriginalUrl());
        dto.setShortUrl(urlMapping.getShortUrl());
        dto.setClickCount(urlMapping.getClickCount());
        dto.setCreatedDate(urlMapping.getCreatedDate());
        if (urlMapping.getUser() != null) {
            dto.setUsername(urlMapping.getUser().getEmail());
        }
        return dto;
    }

    private String generateShortUrl() {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String shortUrl;
        do {
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 8; i++) {
                sb.append(characters.charAt(random.nextInt(characters.length())));
            }
            shortUrl = sb.toString();
        } while (urlMappingRepository.findByShortUrl(shortUrl) != null);
        return shortUrl;
    }
}