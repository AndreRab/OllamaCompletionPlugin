package com.github.andrerab.ollamacompletionplugin.core;

import com.github.andrerab.ollamacompletionplugin.cache.AutoCompleteCache;
import com.github.andrerab.ollamacompletionplugin.config.ErrorMessage;
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest;
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionElement;
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;


public class CompletionEngine {
    public static final int CACHE_WINDOW_SIZE = 20;
    public static final int CACHE_CAPACITY = 100;
    private static final AutoCompleteCache cache = new AutoCompleteCache(CACHE_CAPACITY, CACHE_WINDOW_SIZE);
    private static final OllamaHandler ollamaHandler = new OllamaHandler();

    public static List<InlineCompletionElement> getCompletion(@NotNull InlineCompletionRequest request) {
        String prefix = ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
            int offset = request.getEditor().getCaretModel().getOffset();
            return request.getDocument().getText().substring(0, offset);
        });
        String suggestion = complete(prefix);

        InlineCompletionElement element = new InlineCompletionGrayTextElement(suggestion);
        return Collections.singletonList(element);
    }

    public static String complete(String prefix){
        String storedPostfix = cache.get(prefix);
        if(storedPostfix == null){
            String llamaCompletion = ollamaHandler.requestLlama(prefix).join();
            if(!llamaCompletion.equals(ErrorMessage.ERROR_RESPONSE_FORMAT_MESSAGE) && !llamaCompletion.equals(ErrorMessage.ERROR_AI_MESSAGE)) {
                cache.put(prefix, llamaCompletion);
            }
            return llamaCompletion;
        }
        return storedPostfix;
    }
}
