package com.what.todo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.what.todo.databinding.FragmentCalendarBinding
import com.what.todo.models.Event
import com.what.todo.utils.PrefsHelper
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var prefsHelper: PrefsHelper
    private var selectedDate: Date = Date()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefsHelper = PrefsHelper(requireContext())
        setupCalendarView()
        setupRecyclerView()
        setupAddEventButton()
    }

    private fun setupCalendarView() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.time
            updateEventsList()
        }
    }

    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter(
            onEventClick = { event -> showEventDetails(event) },
            onEventComplete = { event -> toggleEventComplete(event) }
        )
        binding.eventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }
        updateEventsList()
    }

    private fun setupAddEventButton() {
        binding.addEventFab.setOnClickListener {
            showAddEventDialog()
        }
    }

    private fun showAddEventDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(com.what.todo.R.layout.dialog_add_event, null)
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Event")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                // Handle event creation
                val title = dialogView.findViewById<android.widget.EditText>(com.what.todo.R.id.eventTitleInput).text.toString()
                val description = dialogView.findViewById<android.widget.EditText>(com.what.todo.R.id.eventDescriptionInput).text.toString()
                
                showTimePicker { time ->
                    val event = Event(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        date = selectedDate,
                        time = time
                    )
                    saveEvent(event)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select event time")
            .build()

        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val minute = picker.minute
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val timeString = timeFormat.format(calendar.time)
            onTimeSelected(timeString)
        }

        picker.show(parentFragmentManager, "time_picker")
    }

    private fun saveEvent(event: Event) {
        val events = prefsHelper.getEvents().toMutableList()
        events.add(event)
        prefsHelper.saveEvents(events)
        updateEventsList()
    }

    private fun updateEventsList() {
        val allEvents = prefsHelper.getEvents()
        val dateEvents = allEvents.filter { event ->
            val eventCal = Calendar.getInstance().apply { time = event.date }
            val selectedCal = Calendar.getInstance().apply { time = selectedDate }
            
            eventCal.get(Calendar.YEAR) == selectedCal.get(Calendar.YEAR) &&
            eventCal.get(Calendar.MONTH) == selectedCal.get(Calendar.MONTH) &&
            eventCal.get(Calendar.DAY_OF_MONTH) == selectedCal.get(Calendar.DAY_OF_MONTH)
        }
        eventsAdapter.submitList(dateEvents)
    }

    private fun showEventDetails(event: Event) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(event.title)
            .setMessage("Time: ${event.time}\n\n${event.description}")
            .setPositiveButton("OK", null)
            .setNeutralButton("Delete") { _, _ ->
                deleteEvent(event)
            }
            .show()
    }

    private fun toggleEventComplete(event: Event) {
        val events = prefsHelper.getEvents().toMutableList()
        val index = events.indexOfFirst { it.id == event.id }
        if (index != -1) {
            events[index] = event.copy(isCompleted = !event.isCompleted)
            prefsHelper.saveEvents(events)
            updateEventsList()
        }
    }

    private fun deleteEvent(event: Event) {
        val events = prefsHelper.getEvents().toMutableList()
        events.removeAll { it.id == event.id }
        prefsHelper.saveEvents(events)
        updateEventsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 