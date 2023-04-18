package com.example.aiagenda.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchoolClass(
    val id: Int = 0,
    val name: String = " ",
    val percentage: Map<String, String> = emptyMap(),
    val credits: Int = 0,
    val exam: String = " ",
    val teacher: Map<String, String> = emptyMap()
) : Parcelable

data class SchoolClassBody(
    val classes: MutableList<SchoolClass> = mutableListOf()
)