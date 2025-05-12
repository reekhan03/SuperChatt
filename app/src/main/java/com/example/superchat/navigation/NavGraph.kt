package com.example.superchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.superchat.uiapp.ChatListScreen
import com.example.superchat.uiapp.ChatScreen
import com.example.superchat.uiapp.LoginScreen
import com.example.superchat.uiapp.RegisterScreen

@Composable
fun NavGraph(startDestination: String = Screen.Login.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(
            Screen.ChatList.route,
            arguments = listOf(navArgument("currentUserId") { type = NavType.StringType })
        ) { backStackEntry ->
            val currentUserId = backStackEntry.arguments?.getString("currentUserId") ?: return@composable
            ChatListScreen(currentUserId = currentUserId, navController = navController)
        }
        composable(
            Screen.Chat.route,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType },
                navArgument("currentUserId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
            val currentUserId = backStackEntry.arguments?.getString("currentUserId") ?: return@composable
            ChatScreen(chatId = chatId, currentUserId = currentUserId, navController = navController)
        }
    }
}
