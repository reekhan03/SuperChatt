package com.example.superchat.uiapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.superchat.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(
    chatId: String,
    currentUserId: String,
    navController: NavHostController,
    viewModel: ChatViewModel = viewModel()
) {
    val messages by viewModel.messages.observeAsState(emptyList())
    var newMessage by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        viewModel.loadMessages(chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Chat")
                },
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
                BasicTextField(
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
