package com.example.aiagenda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.aiagenda.repository.AuthenticationRepository

class AuthViewModel(val app: Application, private val repository: AuthenticationRepository) :
    AndroidViewModel(app) {

    fun register(email: String, password: String) {
        repository.signUp(email, password)
    }

}