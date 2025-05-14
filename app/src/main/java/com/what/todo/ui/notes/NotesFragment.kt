package com.what.todo.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.what.todo.databinding.FragmentNotesBinding
import com.what.todo.models.Note
import com.what.todo.utils.PrefsHelper
import java.util.*

class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var prefsHelper: PrefsHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefsHelper = PrefsHelper(requireContext())
        setupRecyclerView()
        setupAddNoteButton()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(
            onNoteClick = { note -> showNoteDetails(note) },
            onNoteLongClick = { note -> showDeleteNoteDialog(note) }
        )
        binding.notesRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
        }
        updateNotesList()
    }

    private fun setupAddNoteButton() {
        binding.addNoteFab.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun showAddNoteDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(com.what.todo.R.layout.dialog_add_note, null)
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Note")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = dialogView.findViewById<android.widget.EditText>(com.what.todo.R.id.noteTitleInput).text.toString()
                val content = dialogView.findViewById<android.widget.EditText>(com.what.todo.R.id.noteContentInput).text.toString()
                
                val note = Note(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    date = Date()
                )
                saveNote(note)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNoteDetails(note: Note) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(note.title)
            .setMessage(note.content)
            .setPositiveButton("Edit") { _, _ ->
                showEditNoteDialog(note)
            }
            .setNegativeButton("Close", null)
            .setNeutralButton("Delete") { _, _ ->
                deleteNote(note)
            }
            .show()
    }

    private fun showEditNoteDialog(note: Note) {
        val dialogView = LayoutInflater.from(context)
            .inflate(com.what.todo.R.layout.dialog_add_note, null)
        
        dialogView.findViewById<android.widget.EditText>(com.what.todo.R.id.noteTitleInput).setText(note.title)
        dialogView.findViewById<android.widget.EditText>(com.what.todo.R.id.noteContentInput).setText(note.content)
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Note")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = dialogView.findViewById<android.widget.EditText>(com.what.todo.R.id.noteTitleInput).text.toString()
                val content = dialogView.findViewById<android.widget.EditText>(com.what.todo.R.id.noteContentInput).text.toString()
                
                val updatedNote = note.copy(
                    title = title,
                    content = content,
                    date = Date()
                )
                updateNote(updatedNote)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteNoteDialog(note: Note) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete") { _, _ ->
                deleteNote(note)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveNote(note: Note) {
        val notes = prefsHelper.getNotes().toMutableList()
        notes.add(note)
        prefsHelper.saveNotes(notes)
        updateNotesList()
    }

    private fun updateNote(note: Note) {
        val notes = prefsHelper.getNotes().toMutableList()
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
            prefsHelper.saveNotes(notes)
            updateNotesList()
        }
    }

    private fun deleteNote(note: Note) {
        val notes = prefsHelper.getNotes().toMutableList()
        notes.removeAll { it.id == note.id }
        prefsHelper.saveNotes(notes)
        updateNotesList()
    }

    private fun updateNotesList() {
        val notes = prefsHelper.getNotes().sortedByDescending { it.date }
        notesAdapter.submitList(notes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 