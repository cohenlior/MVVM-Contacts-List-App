package com.lior.phonenumbersapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lior.phonenumbersapp.model.Contact
import com.lior.phonenumbersapp.model.ContactDetailsRepository
import com.lior.phonenumbersapp.model.ContactRepository

/**
 * Factory class for constructing ContactDetailsViewModel with parameter
 */
class ContactDetailsViewModelFactory(
    private val selectedContact: Contact,
    private val detailsRepository: ContactDetailsRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactDetailsViewModel::class.java)) {
            return ContactDetailsViewModel(selectedContact, detailsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}