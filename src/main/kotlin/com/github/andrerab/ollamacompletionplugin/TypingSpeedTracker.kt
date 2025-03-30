package com.github.andrerab.ollamacompletionplugin

import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import com.intellij.openapi.application.ApplicationManager

object TypingSpeedTracker {
    private var lastTime: Long = System.currentTimeMillis()
    private var lastOffset: Int = 0

    fun getSpeedFor(request: InlineCompletionRequest): Double {
        return ApplicationManager.getApplication().runReadAction<Double> {
            val now = System.currentTimeMillis()
            val offset = request.editor.caretModel.offset

            val deltaTime = (now - lastTime)
            val deltaChars = (offset - lastOffset)

            lastTime = now
            lastOffset = offset

            deltaChars * 1000.0 / deltaTime
        }
    }
}