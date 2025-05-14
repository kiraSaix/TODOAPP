package com.what.todo.models

import java.util.Date

data class Todo(
    val id: String,
    val title: String,
    val description: String? = null,
    val dueDate: Date? = null,
    val isCompleted: Boolean = false,
    val priority: Int = 0
) 