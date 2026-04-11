package com.url.URL_Shortner.Repository;


import com.url.URL_Shortner.Entity.UrlMapping;
import com.url.URL_Shortner.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findByShortUrl(String shortUrl);
    List<UrlMapping> findByUser(Users user);
}