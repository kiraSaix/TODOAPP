package com.what.todo.models

import java.util.Date

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val date: Date,
    val color: Int? = null
) 