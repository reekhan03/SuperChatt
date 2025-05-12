package com.example.superchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.superchat.model.Chat
import com.google.firebase.database.*

class ChatListViewModel : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> = _chats

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("chats")

    init {
        fetchChats()
    }

    private fun fetchChats() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatList = mutableListOf<Chat>()
                for (chatSnapshot in snapshot.children) {
                    val chat = chatSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chatList.add(chat.copy(chatId = chatSnapshot.key ?: ""))
                    }
                }
                _chats.value = chatList.sortedByDescending { it.timestamp }
            }

            override fun onCancelled(error: DatabaseError) {
                // Тут можно обработать ошибку, если нужно
            }
        })
    }
}
