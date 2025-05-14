package com.what.todo.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.what.todo.databinding.ItemEventBinding
import com.what.todo.models.Event

class EventsAdapter(
    private val onEventClick: (Event) -> Unit,
    private val onEventComplete: (Event) -> Unit
) : ListAdapter<Event, EventsAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EventViewHolder(
        private val binding: ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEventClick(getItem(position))
                }
            }
        }

        fun bind(event: Event) {
            binding.apply {
                eventTitleText.text = event.title
                eventTimeText.text = event.time
                eventDescriptionText.text = event.description
                root.alpha = if (event.isCompleted) 0.6f else 1.0f
                root.setOnLongClickListener {
                    onEventComplete(event)
                    true
                }
            }
        }
    }

    private class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
} 