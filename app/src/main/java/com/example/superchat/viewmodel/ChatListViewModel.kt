package com.example.superchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superchat.model.Chat
import com.example.superchat.model.ChatWithUser
import com.example.superchat.model.User
import com.google.firebase.database.*




class ChatListViewModel : ViewModel() {

    private val _chatList = MutableLiveData<List<ChatWithUser>>()
    val chatList: LiveData<List<ChatWithUser>> = _chatList

    private val dbRef = FirebaseDatabase.getInstance().getReference("chats")
    private val usersRef = FirebaseDatabase.getInstance().getReference("users")

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = _userList

    fun loadAllUsers(currentUserId: String) {
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (userSnap in snapshot.children) {
                    val user = userSnap.getValue(User::class.java)
                    if (user != null && user.userId != currentUserId) {
                        users.add(user)
                    }
                }
                _userList.value = users
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибок
            }
        })
    }


    // Новый метод для загрузки чатов с участниками
    fun loadChatsWithUsers(currentUserId: String) {
        dbRef.orderByChild("participants/$currentUserId").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val rawChats = mutableListOf<Pair<Chat, String>>()

                    for (chatSnapshot in snapshot.children) {
                        val chat = chatSnapshot.getValue(Chat::class.java)
                        val chatId = chatSnapshot.key ?: continue
                        val otherUserId = chat?.participants?.keys?.firstOrNull { it != currentUserId }

                        if (chat != null && otherUserId != null) {
                            rawChats.add(chat.copy(chatId = chatId) to otherUserId)
                        }
                    }

                    if (rawChats.isEmpty()) {
                        _chatList.value = emptyList()
                        return
                    }

                    val tempList = mutableListOf<ChatWithUser>()
                    var completed = 0

                    for ((chat, otherUserId) in rawChats) {
                        usersRef.child(otherUserId)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(userSnap: DataSnapshot) {
                                    val user = userSnap.getValue(User::class.java)
                                    tempList.add(ChatWithUser(chat, user))
                                    completed++

                                    if (completed == rawChats.size) {
                                        _chatList.value = tempList.sortedByDescending { it.chat.timestamp }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    completed++
                                    if (completed == rawChats.size) {
                                        _chatList.value = tempList.sortedByDescending { it.chat.timestamp }
                                    }
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибки загрузки чатов
                }
            })
    }

}
