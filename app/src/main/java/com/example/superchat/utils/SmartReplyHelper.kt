package com.example.superchat.utils


import com.google.mlkit.nl.smartreply.*
import com.example.superchat.model.Message
import com.google.android.gms.predictondevice.SmartReply
import com.google.mlkit.nl.smartreply.TextMessage

class SmartReplyHelper {

//    private val smartReply = SmartReply.getClient()
//
//    fun getSuggestions(
//        messages: List<Message>,
//        onSuccess: (List<String>) -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val conversation = messages.map {
//            if (it.isLocalUser) {
//                TextMessage.createForLocalUser(it.text, it.timestamp)
//            } else {
//                TextMessage.createForRemoteUser(it.text, it.timestamp, it.userId)
//            }
//        }
//
//        smartReply.suggestReplies(conversation)
//            .addOnSuccessListener { result ->
//                if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
//                    onSuccess(result.suggestions.map { it.text })
//                }
//            }
//            .addOnFailureListener {
//                onFailure(it)
//            }
//    }
}