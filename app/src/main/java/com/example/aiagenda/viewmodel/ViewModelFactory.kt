package com.example.aiagenda.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aiagenda.repository.AuthenticationRepository
import com.example.aiagenda.repository.ProfileRepository
import com.example.aiagenda.repository.SharedPreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ViewModelFactory(app: Application) : ViewModelProvider.Factory {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val appPreferencesRepository: SharedPreferencesRepository =
        SharedPreferencesRepository(app.applicationContext)
    private var storageReference = FirebaseStorage.getInstance().reference
    private val authRepository = AuthenticationRepository(
        auth, database, appPreferencesRepository, storageReference
    )
    private val profileRepository = ProfileRepository(storageReference, database)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(profileRepository) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }

}