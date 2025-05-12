package com.example.superchat.model


data class Chat(
    val chatId: String = "",
    val lastMessage: String = "",
    val timestamp: Long = 0L,
    val participants: Map<String, Boolean> = emptyMap()
)

