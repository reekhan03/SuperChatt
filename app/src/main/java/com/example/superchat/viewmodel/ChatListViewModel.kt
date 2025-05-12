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
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatList = mutableListOf<ChatWithUser>()
                    for (chatSnapshot in snapshot.children) {
                        val chat = chatSnapshot.getValue(Chat::class.java)
                        val chatId = chatSnapshot.key ?: continue
                        val otherUserId = chat?.participants?.keys?.firstOrNull { it != currentUserId }

                        if (otherUserId != null) {
                            usersRef.child(otherUserId)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(userSnap: DataSnapshot) {
                                        val user = userSnap.getValue(User::class.java)
                                        val fullChat = chat?.copy(chatId = chatId)
                                        if (fullChat != null) {
                                            chatList.add(ChatWithUser(fullChat, user))
                                        }
                                        _chatList.value = chatList
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Обработка ошибок, если не удалось загрузить данные о пользователе
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибки загрузки чатов
                }
            })
    }
}
