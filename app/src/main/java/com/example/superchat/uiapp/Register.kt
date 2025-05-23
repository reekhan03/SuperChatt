package com.example.superchat.uiapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.superchat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.example.superchat.navigation.Screen
import com.google.firebase.database.FirebaseDatabase

@Composable
fun RegisterScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                registerUser(email, fullName, password, auth, context, navController)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate(Screen.Login.route) }
        ) {
            Text("Already have an account? Login")
        }
    }
}

fun registerUser(
    email: String,
    fullName: String,
    password: String,
    auth: FirebaseAuth,
    context: Context,
    navController: NavHostController
) {
    val database = FirebaseDatabase.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val uid = user?.uid ?: return@addOnCompleteListener

                // создаём пользователя по твоему data class
                val newUser = User(
                    userId = uid,
                    name = fullName,
                    profilePictureUrl = "" // если пока нет аватарки
                )

                val userRef = database.getReference("users").child(uid)
                userRef.setValue(newUser)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()

                        // переход на ChatList
                        navController.navigate(
                            Screen.ChatList.route.replace("{currentUserId}", uid)
                        ) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(
                    context,
                    "Registration failed: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

