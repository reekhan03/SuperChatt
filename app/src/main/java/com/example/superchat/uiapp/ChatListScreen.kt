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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.superchat.viewmodel.ChatListViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.livedata.observeAsState
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = viewModel()
) {
    val chats by viewModel.chats.observeAsState(emptyList())
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""


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
                ListItem(
                    headlineContent = { Text("Sam", fontWeight = FontWeight.Bold) },
                    supportingContent = { Text(chat.lastMessage) },
                    trailingContent = {
                        val date = Date(chat.timestamp)
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        Text(formatter.format(date), fontSize = 12.sp)
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("chat/${chat.chatId}/${currentUserId}")
                        }
                )
                Divider()
            }
        }
    }
}
