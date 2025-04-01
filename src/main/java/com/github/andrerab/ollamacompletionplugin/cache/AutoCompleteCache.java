package com.github.andrerab.ollamacompletionplugin.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AutoCompleteCache {
    private final Map<String, String> cache;
    private final int cacheWindowSize;

    public AutoCompleteCache(int capacity, int cachelWindowSize) {
        this.cache = Collections.synchronizedMap(new LinkedHashMap<>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > capacity;
            }
        });
        this.cacheWindowSize = cachelWindowSize;
    }

    public String get(String key) {
        return cache.get(key);
    }

    public void put(String key, String value) {
        if (key == null || value == null) return;
        cache.put(key, value);
        cachePrefixVariants(key, value);
        cachePostfixVariants(key, value);
    }

    private void cachePrefixVariants(String key, String value){
        int maxI = Math.min(cacheWindowSize, key.length());
        for (int i = 1; i < maxI; i++) {
            String newKey = key.substring(0, key.length() - i);
            String newValue = key.substring(key.length() - i) + value;
            cache.putIfAbsent(newKey, newValue);
        }
    }

    private void cachePostfixVariants(String key, String value){
        int maxI = Math.min(cacheWindowSize, value.length());
        for (int i = 1; i < maxI; i++) {
            String newKey = key + value.substring(0, i);
            String newValue = value.substring(i);
            cache.putIfAbsent(newKey, newValue);
        }
    }
}
