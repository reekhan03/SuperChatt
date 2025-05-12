package com.example.superchat.uiapp

import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.superchat.viewmodel.ChatViewModel
import com.example.superchat.model.Message
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    currentUserId: String,
    navController: NavHostController,
    viewModel: ChatViewModel = viewModel()
) {
    // Используем observeAsState для получения сообщений
    val messages by viewModel.messages.observeAsState(emptyList())

    var newMessage by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        viewModel.loadMessages(chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = newMessage,
                    onValueChange = { newMessage = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                        .padding(12.dp)
                )
                IconButton(onClick = {
                    if (newMessage.isNotBlank()) {
                        viewModel.sendMessage(chatId, currentUserId, newMessage)
                        newMessage = ""
                    }
                }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Send")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            messages.forEach { message ->
                val isCurrentUser = message.senderId == currentUserId
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        color = if (isCurrentUser) Color.Black else Color.LightGray,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = message.text,
                            color = if (isCurrentUser) Color.White else Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
