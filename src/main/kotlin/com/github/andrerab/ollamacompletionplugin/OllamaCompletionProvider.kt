package com.github.andrerab.ollamacompletionplugin

import com.intellij.codeInsight.inline.completion.*
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSingleSuggestion
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSuggestion
import com.intellij.openapi.progress.coroutineToIndicator
import kotlinx.coroutines.flow.asFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class OllamaCompletionProvider : DebouncedInlineCompletionProvider() {
    override val id: InlineCompletionProviderID
        get() = InlineCompletionProviderID("Ollama")

    override suspend fun getSuggestionDebounced(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val elements = coroutineToIndicator {
            CompletionEngine.getCompletion(request)
        } ?: return InlineCompletionSuggestion.Empty
        return InlineCompletionSingleSuggestion.build(elements = elements.asFlow())
    }

    override fun isEnabled(event: InlineCompletionEvent): Boolean {
        return true
    }

    override suspend fun getDebounceDelay(request: InlineCompletionRequest): Duration {
        val speed = TypingSpeedTracker.getSpeedFor(request)
        return when {
            speed < 2.0 -> 50.milliseconds
            speed < 5.0 -> 75.milliseconds
            else        -> 150.milliseconds
        }
    }

}
