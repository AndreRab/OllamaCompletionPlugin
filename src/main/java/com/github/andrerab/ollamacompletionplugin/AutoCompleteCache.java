package com.github.andrerab.ollamacompletionplugin;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AutoCompleteCache {
    private final int capacity;
    private final Map<String, String> cache;

    public AutoCompleteCache(int capacity) {
        this.capacity = capacity;
        this.cache = Collections.synchronizedMap(new LinkedHashMap<>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > capacity;
            }
        });
    }

    public String get(String key) {
        return cache.get(key);
    }

    public void put(String key, String value) {
        if (key == null || value == null) return;
        cache.put(key, value);

        for (int i = 0; i < value.length(); i++) {
            String newKey = key + value.substring(0, i + 1);
            String newValue = value.substring(i + 1);
            if (!cache.containsKey(newKey)) {
                cache.put(newKey, newValue);
            }
        }
    }
}
