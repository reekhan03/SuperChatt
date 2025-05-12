package com.example.superchat.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.superchat.model.Message
import kotlinx.coroutines.launch


class ChatViewModel : ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun loadMessages(chatId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { snapshot ->
                val loadedMessages = snapshot.documents.mapNotNull { doc ->
                    val message = doc.toObject(Message::class.java)
                    message?.copy(id = doc.id)  // добавляем ID документа
                }
                _messages.value = loadedMessages
            }
            .addOnFailureListener { e ->
                // обработка ошибки
            }
    }

    fun sendMessage(chatId: String, senderId: String, text: String) {
        val db = FirebaseFirestore.getInstance()
        val message = Message(senderId = senderId, text = text, timestamp = System.currentTimeMillis())
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                loadMessages(chatId)  // перезагружаем сообщения после отправки
            }
            .addOnFailureListener { e ->
                // обработка ошибки
            }
    }
}
