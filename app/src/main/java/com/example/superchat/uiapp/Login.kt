package com.example.superchat.uiapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext
import com.example.superchat.navigation.NavGraph


@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current // Получаем контекст для Toast

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
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation() // Маскируем пароль
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                loginUser(email, password, auth, context, navController) // Переходим на другой экран после входа
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка для перехода на экран регистрации
        TextButton(
            onClick = { navController.navigate("register") }
        ) {
            Text("Don't have an account? Register")
        }
    }
}


// Функция для входа с передачей контекста
fun loginUser(
    email: String,
    password: String,
    auth: FirebaseAuth,
    context: Context,
    navController: NavHostController // Передаем NavController для перехода
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()

                // Навигация на экран чатов
                navController.navigate(Screen.ChatListScreen.name){
                    popUpTo(Screen.LoginScreen.name) { inclusive = true }
                } // Переход на экран чатов
            } else {
                Toast.makeText(
                    context,
                    "Login failed: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}




