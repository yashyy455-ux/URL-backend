package com.url.URL_Shortner.listener;

import com.url.URL_Shortner.Event.UrlClickEvent;
import com.url.URL_Shortner.sse.SseManager;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class UrlClickEventListener {

    private final SseManager sseManager;

    @Async
    @EventListener
    public void handleClickEvent(UrlClickEvent event) {

        Map<String, Object> data = new HashMap<>();
        data.put("shortUrl", event.getShortUrl());
        data.put("clickCount", event.getClickCount());

        sseManager.send(event.getUserId(), data);
    }
}