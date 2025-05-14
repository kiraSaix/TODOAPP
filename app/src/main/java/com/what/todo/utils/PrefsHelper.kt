package com.what.todo.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.what.todo.models.Event
import com.what.todo.models.Note
import com.what.todo.models.Todo

class PrefsHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("TodoPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_TODOS = "todos"
        private const val KEY_EVENTS = "events"
        private const val KEY_NOTES = "notes"
    }

    fun saveTodos(todos: List<Todo>) {
        val json = gson.toJson(todos)
        prefs.edit().putString(KEY_TODOS, json).apply()
    }

    fun getTodos(): List<Todo> {
        val json = prefs.getString(KEY_TODOS, null)
        return if (json != null) {
            val type = object : TypeToken<List<Todo>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun saveEvents(events: List<Event>) {
        val json = gson.toJson(events)
        prefs.edit().putString(KEY_EVENTS, json).apply()
    }

    fun getEvents(): List<Event> {
        val json = prefs.getString(KEY_EVENTS, null)
        return if (json != null) {
            val type = object : TypeToken<List<Event>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun saveNotes(notes: List<Note>) {
        val json = gson.toJson(notes)
        prefs.edit().putString(KEY_NOTES, json).apply()
    }

    fun getNotes(): List<Note> {
        val json = prefs.getString(KEY_NOTES, null)
        return if (json != null) {
            val type = object : TypeToken<List<Note>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
} 