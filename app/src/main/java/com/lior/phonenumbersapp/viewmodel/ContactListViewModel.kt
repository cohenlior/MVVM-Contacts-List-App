package com.lior.phonenumbersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lior.phonenumbersapp.model.Contact
import com.lior.phonenumbersapp.model.ContactRepository
import kotlinx.coroutines.*

/**
 * ContactListViewModel designed to store and manage contact list data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as getting all device contacts can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class ContactListViewModel(private val repository: ContactRepository) : ViewModel() {
    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Request status for permission check when the viewModel first constructed.
     * This is private to avoid exposing this value to observers.
     */
    private val _requestStatus = MutableLiveData<Boolean>()
    /**
     * Request status for permission check when the viewModel first constructed.
     * Views should use this to get access to the data.
     */
    val requestStatus: LiveData<Boolean>
        get() = _requestStatus

    init {
        requestContactsPermissionOrStartLoadingList()
    }

    private fun requestContactsPermissionOrStartLoadingList() {
        _requestStatus.value = true
    }

    fun onRequestPermissionComplete() {
        _requestStatus.value = null
    }

    fun startLoadingContacts() {
        viewModelScope.launch {
            repository.getContacts()
        }
    }

    val contacts = repository.contactList

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}