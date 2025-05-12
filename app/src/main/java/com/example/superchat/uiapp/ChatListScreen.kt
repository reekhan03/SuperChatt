package com.example.superchat.uiapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.superchat.model.Chat
import com.example.superchat.viewmodel.ChatListViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    currentUserId: String,
    navController: NavController,
    viewModel: ChatListViewModel = viewModel()
) {
//    val chats by viewModel.chats.observeAsState(emptyList())

    val sampleChats = listOf(
        Chat(
            chatId = "chat1",
            lastMessage = "Привет, как дела?",
            timestamp = System.currentTimeMillis() - 60 * 60 * 1000, // 1 час назад
            participants = mapOf("user1" to true, "user2" to true)
        ),
        Chat(
            chatId = "chat2",
            lastMessage = "Завтра встречаемся в 18:00!",
            timestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000, // 1 день назад
            participants = mapOf("user1" to true, "user3" to true)
        ),
        Chat(
            chatId = "chat3",
            lastMessage = "Окей, договорились!",
            timestamp = System.currentTimeMillis() - 3 * 60 * 1000, // 3 минуты назад
            participants = mapOf("user2" to true, "user4" to true)
        )
    )
    val chats = sampleChats
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Chats") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(chats) { chat ->
                ChatItem(
                    chatTitle = "Sam", // при желании можно заменить на chat.title
                    lastMessage = chat.lastMessage,
                    timestamp = chat.timestamp,
                    onClick = {
                        navController.navigate("chat/${chat.chatId}/${currentUserId}")
                    }
                )
            }
        }
    }
}

@Composable
fun ChatItem(
    chatTitle: String,
    lastMessage: String,
    timestamp: Long,
    onClick: () -> Unit
) {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateString = formatter.format(date)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = chatTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = lastMessage, maxLines = 1, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Text(text = dateString, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Divider()
}
