package com.github.andrerab.ollamacompletionplugin.core;

import com.github.andrerab.ollamacompletionplugin.config.ErrorMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OllamaHandlerTest {
    private static String callDecodeUnicode(String input) throws Exception {
        var method = OllamaHandler.class.getDeclaredMethod("decodeUnicode", String.class);
        method.setAccessible(true);
        return (String) method.invoke(null, input);
    }

    private static String callExtractAnswer(String input) throws Exception {
        var method = OllamaHandler.class.getDeclaredMethod("extractAnswer", String.class);
        method.setAccessible(true);
        return (String) method.invoke(null, input);
    }

    private static String callCreateBody(String prefix) throws Exception {
        var method = OllamaHandler.class.getDeclaredMethod("createBody", String.class);
        method.setAccessible(true);
        return (String) method.invoke(null, prefix);
    }

    @Test
    public void testDecodeUnicode_withEscapes() throws Exception {
        String input = "Hello\\u0020World\\u0021";
        String expected = "Hello World!";
        assertEquals(expected, callDecodeUnicode(input));
    }

    @Test
    public void testDecodeUnicode_withoutEscapes() throws Exception {
        String input = "No unicode here";
        assertEquals(input, callDecodeUnicode(input));
    }

    @Test
    public void testExtractAnswer_validJsonWithAnswer() throws Exception {
        String json = """
            {
              "response": "Final answer: System.out.println(\\\"Hi!\\\");\\n", "done": true
            }
        """;
        assertEquals("System.out.println(\"Hi!\");", callExtractAnswer(json));
    }

    @Test
    public void testExtractAnswer_validJsonWithoutAnswer() throws Exception {
        String json = """
            {
              "response": "Some text without the keyword", "done": true
            }
        """;
        assertEquals(ErrorMessage.ERROR_AI_MESSAGE, callExtractAnswer(json));
    }

    @Test
    public void testExtractAnswer_invalidJson() throws Exception {
        String json = """
            {
              "no_response_here": "oops"
            }
        """;
        assertEquals(ErrorMessage.ERROR_RESPONSE_FORMAT_MESSAGE, callExtractAnswer(json));
    }

    @Test
    public void testCreateBody_escapeAndFormat() throws Exception {
        String input = "System.out.println(\"Hello\");\n";
        String body = callCreateBody(input);

        assertTrue(body.contains("\"prompt\":"));
        assertTrue(body.contains("\\\"Hello\\\""));
        assertTrue(body.contains("\\n"));
        assertTrue(body.contains("deepseek-coder"));
    }
}
