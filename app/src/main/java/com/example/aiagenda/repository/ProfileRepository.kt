package com.example.aiagenda.repository

import android.net.Uri
import android.util.Log
import com.example.aiagenda.model.User
import com.example.aiagenda.util.FireStoreCollection
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

class ProfileRepository(
    private val storageFirebase: StorageReference,
    private val database: FirebaseFirestore
) {

    fun uploadPhoto(photoUri: Uri, user: User, onResult: (Uri) -> Unit) {
        val imageRef =
            storageFirebase.child("images/" + user.id + "/" + user.id)
        val uploadTask = imageRef.putFile(photoUri)
        uploadTask.addOnSuccessListener {
            val downloadUrl = imageRef.downloadUrl
            downloadUrl.addOnSuccessListener { uri ->
                onResult.invoke(uri)
            }
        }
    }

    fun updateUser(photoUri: Uri, user: User) {
        val document = database.collection(FireStoreCollection.USER).document(user.id)
            .update("photo_url", photoUri)

        document
            .addOnSuccessListener {
                Log.e("UPDATEUSER", "SUCCESS")
            }
            .addOnFailureListener {
                Log.e("UPDATEUSER", "ERROR")
            }
    }

}