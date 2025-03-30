package com.github.andrerab.ollamacompletionplugin;

import com.intellij.codeInsight.inline.completion.InlineCompletionRequest;
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionElement;
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;


public class CompletionEngine {
    private static final int cacheSize = 100_000;
    private static final AutoCompleteCache cache = new AutoCompleteCache(cacheSize);
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
            if(llamaCompletion != ErrorMessage.ERROR_RESPONSE_FORMAT_MESSAGE && llamaCompletion != ErrorMessage.ERROR_AI_MESSAGE) {
                cache.put(prefix, llamaCompletion);
            }
            return llamaCompletion;
        }
        return storedPostfix;
    }
}
