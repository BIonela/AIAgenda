package com.example.aiagenda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.aiagenda.repository.AuthenticationRepository
import com.example.aiagenda.util.AuthenticationStatus
import com.google.android.material.textfield.TextInputLayout

class AuthViewModel(val app: Application, val repository: AuthenticationRepository) :
    AndroidViewModel(app) {

    fun register(email: String, password: String, passwordLayout: TextInputLayout) {
        if (password.length < 6) {
            passwordLayout.error = "Parola trebuie sa contina minim 6 caractere"
        } else {
            repository.signUp(email, password)
        }
    }

}