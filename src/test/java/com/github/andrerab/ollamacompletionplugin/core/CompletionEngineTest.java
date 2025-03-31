package com.github.andrerab.ollamacompletionplugin.core;

import com.github.andrerab.ollamacompletionplugin.config.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompletionEngineTest {
    private OllamaHandler ollamaHandler;
    @BeforeEach
    public void setUp() throws Exception {
        ollamaHandler = mock(OllamaHandler.class);

        Field handlerField = CompletionEngine.class.getDeclaredField("ollamaHandler");
        handlerField.setAccessible(true);
        handlerField.set(null, ollamaHandler);

        Field cacheField = CompletionEngine.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
    }

    @Test
    public void testComplete_CacheMiss_ValidResponse() {
        when(ollamaHandler.requestLlama("System.out"))
                .thenReturn(CompletableFuture.completedFuture("println();"));

        String result = CompletionEngine.complete("System.out");
        assertEquals("println();", result);

        String cached = CompletionEngine.complete("System.out");
        verify(ollamaHandler, times(1)).requestLlama("System.out");
        assertEquals("println();", cached);
    }

    @Test
    void testComplete_CacheMiss_ErrorResponse() {
        when(ollamaHandler.requestLlama("invalid"))
                .thenReturn(CompletableFuture.completedFuture(ErrorMessage.ERROR_AI_MESSAGE));

        String result = CompletionEngine.complete("invalid");
        assertEquals(ErrorMessage.ERROR_AI_MESSAGE, result);

        String resultAgain = CompletionEngine.complete("invalid");
        verify(ollamaHandler, times(2)).requestLlama("invalid");
    }

}
