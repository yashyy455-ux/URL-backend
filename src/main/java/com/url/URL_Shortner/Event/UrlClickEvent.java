package com.url.URL_Shortner.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UrlClickEvent {
    private final Long userId;
    private final String shortUrl;
    private final int clickCount;
}