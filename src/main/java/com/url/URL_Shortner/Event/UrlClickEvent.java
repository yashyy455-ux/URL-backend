package com.url.URL_Shortner.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class UrlClickEvent implements Serializable {
    private final Long userId;
    private final String shortUrl;
    private final int clickCount;
}