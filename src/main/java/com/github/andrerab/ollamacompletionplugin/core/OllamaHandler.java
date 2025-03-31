package com.github.andrerab.ollamacompletionplugin.core;

import com.github.andrerab.ollamacompletionplugin.config.ErrorMessage;
import com.github.andrerab.ollamacompletionplugin.config.ModelConstants;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.codeInspection.InspectionApplicationBase.LOG;

public class OllamaHandler {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Pattern RESPONSE_PATTERN = Pattern.compile(ModelConstants.RESPONSE_PATTERN_STRING);
    private static final Pattern UNICODE_PATTERN = Pattern.compile(ModelConstants.UNICODE_PATTERN_STRING);
    private static final Pattern ANSWER_PATTERN = Pattern.compile(ModelConstants.ANSWER_PATTERN_STRING);

    public CompletableFuture<String> requestLlama(String prefix){
        String body = createBody(prefix);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ModelConstants.MODEL_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(OllamaHandler::extractAnswer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull String decodeUnicode(String input) {
        if (!input.contains("\\u")) {
            return input;
        }
        StringBuilder result = new StringBuilder(input.length());
        Matcher matcher = UNICODE_PATTERN.matcher(input);
        int last = 0;
        while (matcher.find()) {
            result.append(input, last, matcher.start());
            int codePoint = Integer.parseInt(matcher.group(1), 16);
            result.append((char) codePoint);
            last = matcher.end();
        }
        if (last < input.length()) {
            result.append(input, last, input.length());
        }
        return result.toString();
    }

    private static String extractAnswer(String responseJson) {
        LOG.info("response: " + responseJson);
        Matcher matcher = RESPONSE_PATTERN.matcher(responseJson);
        if (!matcher.find()) {
            return ErrorMessage.ERROR_RESPONSE_FORMAT_MESSAGE;
        }
        String responseText = matcher.group(1)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
        responseText = decodeUnicode(responseText);
        Matcher answerMatcher = ANSWER_PATTERN.matcher(responseText);
        if (answerMatcher.find()) {
            String answer = answerMatcher.group(1);
            int newlineIndex = answer.indexOf('\n');
            return (newlineIndex > 0) ? answer.substring(0, newlineIndex).trim() : answer.trim();
        }

        return ErrorMessage.ERROR_AI_MESSAGE;
    }

    private static @NotNull String createBody(String prefix){
        String escaped = (ModelConstants.SYSTEM_PROMPT + prefix)
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
        LOG.info("Prompt: " + escaped);
        return """
            {
                "model": "deepseek-coder:6.7b",
                "prompt": "%s",
                "stream": false
            }
            """.formatted(escaped);
    }
}
