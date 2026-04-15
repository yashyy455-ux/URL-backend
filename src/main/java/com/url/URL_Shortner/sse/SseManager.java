package com.url.URL_Shortner.sse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseManager {

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(0L);

        emitters
                .computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
                .add(emitter);

        emitter.onCompletion(() -> remove(userId, emitter));
        emitter.onTimeout(() -> remove(userId, emitter));
        emitter.onError(e -> remove(userId, emitter));

        return emitter;
    }

    private void remove(Long userId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(userId);
        if (list != null) {
            list.remove(emitter);
        }
    }

    public void send(Long userId, Object data) {
        List<SseEmitter> list = emitters.get(userId);

        if (list == null) return;

        for (SseEmitter emitter : list) {
            try {
                emitter.send(data);
            } catch (IOException e) {
                remove(userId, emitter);
            }
        }
    }
}