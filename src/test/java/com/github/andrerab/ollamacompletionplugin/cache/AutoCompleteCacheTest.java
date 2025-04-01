package com.github.andrerab.ollamacompletionplugin.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AutoCompleteCacheTest {
    private AutoCompleteCache cache;
    @BeforeEach
    public void setup() {
        cache = new AutoCompleteCache(10, 3);
    }
    @Test
    public void basicPutTest() {
        cache.put("System.out.", "println();");
        assertEquals("println();", cache.get("System.out."));
    }

    @Test
    public void testPrefixVariants() {
        cache.put("System.out.", "println");
        assertEquals(".println", cache.get("System.out"));
        assertEquals("t.println", cache.get("System.ou"));
        assertNull(cache.get("Syst"));
    }

    @Test
    public void testPostfixVariants() {
        cache.put("System.out.", "println");
        assertEquals("rintln", cache.get("System.out.p"));
        assertEquals("intln", cache.get("System.out.pr"));
        assertNull(cache.get("System.out.prin"));
    }

    @Test
    public void testEvictionPolicy() {
        AutoCompleteCache smallCache = new AutoCompleteCache(2, 1);
        smallCache.put("a", "x");
        smallCache.put("b", "y");
        smallCache.put("c", "z");
        assertNull(smallCache.get("a"));
        assertEquals("y", smallCache.get("b"));
        assertEquals("z", smallCache.get("c"));
    }

}
