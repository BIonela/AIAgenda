package com.example.aiagenda.repository

import com.example.aiagenda.model.Course
import com.example.aiagenda.model.Timetable
import com.example.aiagenda.model.TimetableTime
import com.example.aiagenda.model.User
import com.example.aiagenda.util.UserDataStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class TimetableRepository(
    private val database: FirebaseFirestore
) {

    fun getCourses(user: User, result: (Timetable) -> Unit, uiState: (UserDataStatus) -> Unit) {
        val studyYear = user.study_year
        val docRef =
            database.collection("timetable").document("year${studyYear}")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val document = documentSnapshot.toObject<Timetable>()
                if (document != null) {
                    result.invoke(document)
                    uiState.invoke(UserDataStatus.SUCCESS)
                }
            }
            .addOnFailureListener {
                uiState.invoke(UserDataStatus.ERROR)
            }
    }

    fun getTimetableTime(
        result: (Map<String, TimetableTime>) -> Unit,
        uiState: (UserDataStatus) -> Unit
    ) {
        val docRef =
            database.collection("timeData")
        docRef.get()
            .addOnSuccessListener { document ->
                val timeTimetable = mutableMapOf<String, TimetableTime>()
                for (item in document) {
                    val time = item.toObject<TimetableTime>()
                    timeTimetable[item.id] = time
                }
                result.invoke(timeTimetable)

            }
            .addOnFailureListener {
                uiState.invoke(UserDataStatus.ERROR)
            }
    }

    fun getGroupCourses(groupName: String, isOdd: String, result: (ArrayList<Course>) -> Unit) {
        val docRef = database.collection("timetable").document("year1")
            .collection("group${groupName}${isOdd}")
        docRef
            .get()
            .addOnSuccessListener() { document ->
                val timeTimetable = arrayListOf<Course>()
                for (item in document) {
                    val time = item.toObject<Course>()
                    timeTimetable.add(time)
                }
                result.invoke(timeTimetable)
            }
    }

}