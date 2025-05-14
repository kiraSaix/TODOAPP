package com.what.todo.models

import java.util.Date

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: Date,
    val time: String,
    val isCompleted: Boolean = false
) 