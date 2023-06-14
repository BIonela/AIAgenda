package com.example.aiagenda.model

data class Post(
    val id: String = " ",
    val type: String = " ",
    val title: String = " ",
    val date: String = " ",
    val hour: String = " "
)

data class PostBody(
    val posts: MutableList<Post> = mutableListOf()
)