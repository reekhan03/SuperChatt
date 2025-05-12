package com.example.superchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superchat.model.Message
import com.google.firebase.database.*

class ChatViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    fun loadMessages(chatId: String) {
        val messagesRef = database.child("chats").child(chatId).child("messages")

        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                message?.let {
                    val currentList = _messages.value?.toMutableList() ?: mutableListOf()
                    currentList.add(it)
                    _messages.value = currentList.sortedBy { msg -> msg.timestamp }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

    }

    fun sendMessage(chatId: String, senderId: String, text: String) {
        val messageId = database.child("chats").child(chatId).child("messages").push().key ?: return
        val timestamp = System.currentTimeMillis()

        val message = Message(
            id = messageId,
            senderId = senderId,
            text = text,
            timestamp = timestamp
        )

        database.child("chats").child(chatId).child("messages").child(messageId).setValue(message)
    }
}
