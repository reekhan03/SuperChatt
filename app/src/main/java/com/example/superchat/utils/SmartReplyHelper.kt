package com.example.superchat.utils

import android.util.Log
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestion
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage
import com.google.mlkit.nl.smartreply.*


fun generateSmartReplies(chatHistory: List<Pair<String, String>>, currentUserId: String, onResult: (List<String>) -> Unit) {
    val conversation = chatHistory.map { (senderId, message) ->
        if (senderId == currentUserId)
            TextMessage.createForLocalUser(message, System.currentTimeMillis())
        else
            TextMessage.createForRemoteUser(message, System.currentTimeMillis(), senderId)
    }

    SmartReply.getClient()
        .suggestReplies(conversation)
        .addOnSuccessListener { result ->
            if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                val suggestions = result.suggestions.map { it.text }
                onResult(suggestions)
            }
        }
        .addOnFailureListener {
            onResult(emptyList())
        }
}
