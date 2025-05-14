package com.what.todo.ui.tasks

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.what.todo.R
import com.what.todo.TodoAdapter
import com.what.todo.TodoRepository
import com.what.todo.Todo
import com.what.todo.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var todoRepository: TodoRepository
    private val todos = mutableListOf<Todo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        todoRepository = TodoRepository(requireContext())
        
        setupRecyclerView()
        setupAddButton()
        loadSavedTodos()
        startHeaderAnimation()
    }

    private fun loadSavedTodos() {
        todos.clear()
        todos.addAll(todoRepository.loadTodos())
        todoAdapter.notifyDataSetChanged()
    }

    private fun saveTodos() {
        todoRepository.saveTodos(todos)
    }

    private fun startHeaderAnimation() {
        val moveUpDown = ObjectAnimator.ofFloat(binding.headerImage, View.TRANSLATION_Y, 0f, -20f).apply {
            duration = 1000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val rotation = ObjectAnimator.ofFloat(binding.headerImage, View.ROTATION, -5f, 5f).apply {
            duration = 2000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        AnimatorSet().apply {
            playTogether(moveUpDown, rotation)
            start()
        }
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter(
            todos,
            onTodoCheckedChanged = { todo ->
                todo.isCompleted = !todo.isCompleted
                saveTodos()
                showToast(if (todo.isCompleted) getString(R.string.task_completed) else getString(R.string.keep_going))
            },
            onTodoDeleted = { todo ->
                val position = todos.indexOf(todo)
                todos.removeAt(position)
                todoAdapter.notifyItemRemoved(position)
                saveTodos()
                showToast(getString(R.string.task_removed))
            }
        )

        binding.todoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todoAdapter
        }
    }

    private fun setupAddButton() {
        binding.addButton.setOnClickListener {
            val todoText = binding.todoInput.text.toString().trim()
            if (todoText.isNotEmpty()) {
                val todo = Todo(text = todoText)
                todos.add(0, todo)
                todoAdapter.notifyItemInserted(0)
                binding.todoInput.text.clear()
                saveTodos()
            } else {
                showToast(getString(R.string.enter_task))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        saveTodos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 