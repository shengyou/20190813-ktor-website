package com.example.model

import org.joda.time.DateTime

data class Post(
    val id: Int?,
    val title: String,
    val content: String,
    val createdAt: DateTime,
    val updatedAt: DateTime
)