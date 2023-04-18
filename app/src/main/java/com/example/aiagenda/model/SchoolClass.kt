package com.example.aiagenda.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchoolClass(
    val id: Int = 0,
    val name: String = " ",
    val shortName: String = " ",
    val percentage: Map<String, String> = emptyMap(),
    val credits: Int = 0,
    val exam: String = " ",
    val teacher: Map<String, String> = emptyMap(),
    val courses: List<PdfClass> = emptyList()
) : Parcelable

@Parcelize
data class PdfClass(
    val name: String = " ",
    val url: String = " "
) : Parcelable

data class SchoolClassBody(
    val classes: MutableList<SchoolClass> = mutableListOf()
)