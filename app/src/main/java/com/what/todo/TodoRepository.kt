package com.what.todo

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TodoRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTodos(todos: List<Todo>) {
        val json = gson.toJson(todos)
        prefs.edit().putString("todos", json).apply()
    }

    fun loadTodos(): List<Todo> {
        val json = prefs.getString("todos", null) ?: return emptyList()
        val type = object : TypeToken<List<Todo>>() {}.type
        return gson.fromJson(json, type)
    }
} 