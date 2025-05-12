package com.example.superchat.uiapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.superchat.model.Chat
import com.example.superchat.navigation.Screen
import com.example.superchat.viewmodel.ChatListViewModel
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(currentUserId: String, navController: NavHostController) {
    val usersRef = FirebaseDatabase.getInstance().getReference("users")
    val users = remember { mutableStateListOf<Pair<String, String>>() } // uid to email


    LaunchedEffect(true) {
        usersRef.get().addOnSuccessListener { snapshot ->
            users.clear()
            for (child in snapshot.children) {
                val uid = child.key ?: continue
                val email = child.child("email").getValue(String::class.java) ?: continue
                if (uid != currentUserId) {
                    users.add(uid to email)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Users:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        users.forEach { (uid, email) ->
            Button(
                onClick = {
                    createOrOpenChat(currentUserId, uid) { chatId ->
                        navController.navigate(Screen.Chat.route + "/$chatId/$currentUserId")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(email)
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

fun createOrOpenChat(currentUserId: String, otherUserId: String, onChatReady: (String) -> Unit) {
    val chatRef = FirebaseDatabase.getInstance().getReference("chats")

    chatRef.get().addOnSuccessListener { snapshot ->
        var existingChatId: String? = null

        for (chatSnap in snapshot.children) {
            val participants = chatSnap.child("participants").value as? Map<*, *>
            if (participants != null &&
                participants.containsKey(currentUserId) &&
                participants.containsKey(otherUserId)
            ) {
                existingChatId = chatSnap.key
                break
            }
        }

        if (existingChatId != null) {
            // Чат уже существует
            onChatReady(existingChatId!!)
        } else {
            // Создаем новый чат
            val newChatRef = chatRef.push()
            val participants = mapOf(currentUserId to true, otherUserId to true)
            val chatData = mapOf(
                "participants" to participants,
                "lastMessage" to "",
                "timestamp" to System.currentTimeMillis()
            )
            newChatRef.setValue(chatData).addOnSuccessListener {
                onChatReady(newChatRef.key!!)
            }
        }
    }
}

