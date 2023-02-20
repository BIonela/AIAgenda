package com.example.aiagenda.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.aiagenda.model.User
import com.example.aiagenda.repository.AuthenticationRepository
import com.example.aiagenda.repository.ProfileRepository

class ProfileViewModel(val repository: ProfileRepository) : ViewModel() {

    fun uploadPhoto(photoUri: Uri, user: User, onResult: (Uri) -> Unit) {
        repository.uploadPhoto(
            photoUri = photoUri,
            user = user
        ) {
            onResult.invoke(
                it
            )
        }
    }

    fun updateUser(photoUri: Uri, user: User) {
        repository.updateUser(photoUri, user)
    }

}
