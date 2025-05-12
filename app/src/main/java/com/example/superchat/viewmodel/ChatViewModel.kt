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
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch


//class ChatViewModel : ViewModel() {
//    private val _messages = MutableLiveData<List<Message>>()
//    val messages: LiveData<List<Message>> get() = _messages
//
//    fun loadMessages(chatId: String) {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("chats")
//            .document(chatId)
//            .collection("messages")
//            .orderBy("timestamp")
//            .get()
//            .addOnSuccessListener { snapshot ->
//                val loadedMessages = snapshot.documents.mapNotNull { doc ->
//                    val message = doc.toObject(Message::class.java)
//                    message?.copy(id = doc.id)  // добавляем ID документа
//                }
//                _messages.value = loadedMessages
//            }
//            .addOnFailureListener { e ->
//                // обработка ошибки
//            }
//    }
//
//    fun sendMessage(chatId: String, senderId: String, text: String) {
//        val db = FirebaseFirestore.getInstance()
//        val message = Message(senderId = senderId, text = text, timestamp = System.currentTimeMillis())
//        db.collection("chats")
//            .document(chatId)
//            .collection("messages")
//            .add(message)
//            .addOnSuccessListener {
//                loadMessages(chatId)  // перезагружаем сообщения после отправки
//            }
//            .addOnFailureListener { e ->
//                // обработка ошибки
//            }
//    }
//}

class ChatViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    private var listenerRegistration: ListenerRegistration? = null

    fun loadMessages(chatId: String) {
        listenerRegistration?.remove() // если раньше был слушатель — убираем

        listenerRegistration = db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    return@addSnapshotListener
                }

                val msgs = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Message::class.java)
                }
                _messages.postValue(msgs)
            }
    }

    fun sendMessage(chatId: String, senderId: String, text: String) {
        val message = Message(senderId = senderId, text = text, timestamp = System.currentTimeMillis())

        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
        // теперь не нужно вызывать loadMessages — реальное время уже настроено
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}

