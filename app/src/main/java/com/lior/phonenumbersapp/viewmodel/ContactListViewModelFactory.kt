package com.lior.phonenumbersapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lior.phonenumbersapp.model.ContactRepository
/**
 * Factory class for constructing ContactListViewModel with parameter
 */
class ContactListViewModelFactory(
    private val contactRepository: ContactRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactListViewModel::class.java)) {
            return ContactListViewModel(contactRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}