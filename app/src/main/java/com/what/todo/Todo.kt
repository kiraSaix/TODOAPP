package com.what.todo

data class Todo(
    val id: Long = System.currentTimeMillis(),
    var text: String,
    var isCompleted: Boolean = false,
    var dueDate: Long? = null,
    var priority: Priority = Priority.NORMAL
)

enum class Priority {
    HIGH, NORMAL, LOW
} 