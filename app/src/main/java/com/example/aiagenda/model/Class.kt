package com.example.aiagenda.model

data class Class(
    val id: Int = 0,
    val name: String = " "
)

data class ClassBody(
    val classes: MutableList<Class> = mutableListOf()
)