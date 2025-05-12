package com.example.superchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavGraph
import com.example.superchat.navigation.NavGraph
import com.example.superchat.ui.theme.SuperChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperChatTheme {
                NavGraph() // Вызов навигации
            }
        }
    }
}
