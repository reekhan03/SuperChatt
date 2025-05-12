package com.example.superchat.model


data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val id: String = "" // добавляем поле id для хранения ID документа
)

