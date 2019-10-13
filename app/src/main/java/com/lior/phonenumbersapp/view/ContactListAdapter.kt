package com.lior.phonenumbersapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lior.phonenumbersapp.databinding.ListItemContactBinding
import com.lior.phonenumbersapp.model.Contact

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */
class ContactListAdapter(private val clickListener: OnClickListener) : ListAdapter<Contact, ContactListAdapter.ViewHolder>(DiffCallback) {
    companion object  DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
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
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = getItem(position)
        holder.binding.contactImage.setOnClickListener {
            clickListener.onClickImage(contact)
        }
        holder.binding.clickableOverlay.setOnClickListener {
            clickListener.onClickContact(contact)
        }
        holder.bind(contact)
    }

    /**
     * ViewHolder for Contact items. All work is done by data binding.
     */
    class ViewHolder private constructor(internal val binding: ListItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Contact) {
            binding.contact = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ListItemContactBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(view)
            }
        }
    }
    /**
     * Click listener for Contacts.
     */
    interface OnClickListener {
        fun onClickImage(contact: Contact) {}
        fun onClickContact(contact: Contact) {}
    }
}
