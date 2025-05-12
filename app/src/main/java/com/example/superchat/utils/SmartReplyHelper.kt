package com.example.superchat.utils

import android.util.Log
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestion
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage


// Инициализация SmartReply
val smartReply = SmartReply.getClient()
fun generateSmartReplies() {
    val conversation = listOf(
        TextMessage.createForLocalUser("Привет, как дела?", System.currentTimeMillis()),
        TextMessage.createForRemoteUser("Все отлично, спасибо! А у тебя?", System.currentTimeMillis(), "user1")
    )

    SmartReply.getClient().suggestReplies(conversation)
        .addOnSuccessListener { result: SmartReplySuggestionResult ->
            if (result.status == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                Log.d("SmartReply", "Язык не поддерживается.")
            } else if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                result.suggestions.forEach { suggestion ->
                    Log.d("SmartReply", "Suggested reply: ${suggestion.text}")
                }
            }
        }
        .addOnFailureListener { e ->
            Log.e("SmartReply", "Ошибка: ", e)
        }
}
