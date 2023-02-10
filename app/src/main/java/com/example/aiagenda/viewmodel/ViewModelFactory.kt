package com.example.aiagenda.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aiagenda.repository.AuthenticationRepository
import com.example.aiagenda.repository.SharedPreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewModelFactory(private val app: Application) : ViewModelProvider.Factory {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val appPreferencesRepository: SharedPreferencesRepository =
        SharedPreferencesRepository(app.applicationContext)
    private val repository = AuthenticationRepository(
        auth, database, appPreferencesRepository
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(app, repository) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }

}