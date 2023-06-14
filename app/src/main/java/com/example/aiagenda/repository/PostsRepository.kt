package com.example.aiagenda.repository

import android.util.Log
import com.example.aiagenda.model.*
import com.example.aiagenda.util.FireStoreCollection
import com.example.aiagenda.util.UiStatus
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class PostsRepository(
    private val database: FirebaseFirestore
) {

    fun getPosts(user: User, result: (PostBody) -> Unit, uiStatus: (UiStatus) -> Unit) {
        val docRef =
            database.collection(FireStoreCollection.POSTS).document("posts${user.study_year}")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->

                val item = documentSnapshot.toObject<PostBody>()
                if (item != null) {
                    result.invoke(item)
                    uiStatus.invoke(UiStatus.SUCCESS)
                }

            }
            .addOnFailureListener {
                uiStatus.invoke(UiStatus.ERROR)
            }
    }

    fun addPost(user: User, post: Post, uiState: (UiStatus) -> Unit) {
        val documentRef = database.collection("posts").document("posts${user.study_year}")
        documentRef
            .update("posts", FieldValue.arrayUnion(post))
            .addOnSuccessListener {
                uiState.invoke(UiStatus.SUCCESS)
            }
            .addOnFailureListener {
                uiState.invoke(UiStatus.ERROR)
            }
    }
}