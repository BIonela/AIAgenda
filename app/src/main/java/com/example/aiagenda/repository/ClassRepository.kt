package com.example.aiagenda.repository

import com.example.aiagenda.model.Grade
import com.example.aiagenda.model.SchoolClassBody
import com.example.aiagenda.model.User
import com.example.aiagenda.util.FireStoreCollection
import com.example.aiagenda.util.UiStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.example.aiagenda.model.SchoolClass

class ClassRepository(
    private val database: FirebaseFirestore
) {

    fun getClasses(user: User, result: (List<SchoolClass>) -> Unit, uiState: (UiStatus) -> Unit) {
        val studyYear = user.study_year
        val docRef =
            database.collection(FireStoreCollection.CLASSES).document("classes${studyYear}")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val document = documentSnapshot.toObject<SchoolClassBody>()
                if (document != null) {
                    result.invoke(document.classes)
                    uiState.invoke(UiStatus.SUCCESS)
                }
            }
            .addOnFailureListener {
                uiState.invoke(UiStatus.ERROR)
            }
    }

    fun setGrade(user: User, grades: List<Grade>, uiState: (UiStatus) -> Unit) {
        val docRef = database.collection(FireStoreCollection.USER).document(user.id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    docRef.update("grades", grades)
                        .addOnSuccessListener {
                            uiState.invoke(UiStatus.SUCCESS)
                        }
                        .addOnFailureListener {
                            uiState.invoke(UiStatus.ERROR)
                        }
                } else {
                    uiState.invoke(UiStatus.ERROR)
                }
            }
            .addOnFailureListener {
                uiState.invoke(UiStatus.ERROR)
            }
    }

}