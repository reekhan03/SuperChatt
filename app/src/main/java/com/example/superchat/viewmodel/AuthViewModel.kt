package com.example.superchat.viewmodel


import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _user = MutableStateFlow(auth.currentUser)
    val user = _user.asStateFlow()

    fun signIn(email: String, password: String, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _user.value = auth.currentUser
            }
            .addOnFailureListener {
                onError(it.message ?: "Ошибка входа")
            }
    }

    fun register(email: String, password: String, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _user.value = auth.currentUser
            }
            .addOnFailureListener {
                onError(it.message ?: "Ошибка регистрации")
            }
    }

    fun logout() {
        auth.signOut()
        _user.value = null
    }
}
