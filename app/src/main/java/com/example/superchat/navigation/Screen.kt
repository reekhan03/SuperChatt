package com.example.superchat.navigation


// в navigation/Screen.kt

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ChatList : Screen("chatList/{currentUserId}")
    object Chat : Screen("chat/{chatId}/{currentUserId}")
}

