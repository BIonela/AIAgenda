package com.example.aiagenda.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aiagenda.repository.AuthenticationRepository

class ViewModelFactory(private val app: Application) : ViewModelProvider.Factory {

    private val repository = AuthenticationRepository(app)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(app, repository) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }

}