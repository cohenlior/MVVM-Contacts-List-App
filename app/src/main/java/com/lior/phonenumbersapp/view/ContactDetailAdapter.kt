package com.lior.phonenumbersapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lior.phonenumbersapp.databinding.ListItemLogBinding
import com.lior.phonenumbersapp.model.Call
/**
 * RecyclerView Adapter for setting up data binding on a specific contact.
 */
class ContactDetailAdapter :
    ListAdapter<Call, ContactDetailAdapter.ViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Call>() {
        override fun areItemsTheSame(oldItem: Call, newItem: Call): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Call, newItem: Call): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the log list of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val callHistory = getItem(position)
        holder.bind(callHistory)
    }

    /**
     * ViewHolder for log history items. All work is done by data binding.
     */
    class ViewHolder private constructor(internal val binding: ListItemLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Call) {
            binding.callHistory = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ListItemLogBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(view)
            }
        }
    }
}