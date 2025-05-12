package com.example.superchat.model


data class Message(
    val text: String = "",
    val senderId: String = "",
    val timestamp: Long = 0L
)
