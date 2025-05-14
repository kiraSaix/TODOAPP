package com.what.todo

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private val todos: List<Todo>,
    private val onTodoCheckedChanged: (Todo) -> Unit,
    private val onTodoDeleted: (Todo) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoText: TextView = view.findViewById(R.id.todoText)
        val todoCheckBox: CheckBox = view.findViewById(R.id.todoCheckBox)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]
        
        holder.todoText.text = todo.text
        holder.todoCheckBox.isChecked = todo.isCompleted

        // Strike through text if completed
        if (todo.isCompleted) {
            holder.todoText.paintFlags = holder.todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.todoText.paintFlags = holder.todoText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.todoCheckBox.setOnCheckedChangeListener { _, _ ->
            onTodoCheckedChanged(todo)
        }

        holder.deleteButton.setOnClickListener {
            onTodoDeleted(todo)
        }
    }

    override fun getItemCount() = todos.size
} 