package com.example.aiagenda.repository

import android.util.Log
import com.example.aiagenda.model.PostBody
import com.example.aiagenda.model.TaskBody
import com.example.aiagenda.model.User
import com.example.aiagenda.util.FireStoreCollection
import com.example.aiagenda.util.UiStatus
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
}