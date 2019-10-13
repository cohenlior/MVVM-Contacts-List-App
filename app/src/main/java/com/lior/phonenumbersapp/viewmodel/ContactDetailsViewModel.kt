package com.lior.phonenumbersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lior.phonenumbersapp.model.Contact
import com.lior.phonenumbersapp.model.ContactDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ContactDetailsViewModel designed to store and manage single contact data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as getting call log history can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class ContactDetailsViewModel(
    private val selectedContact: Contact,
    private val repository: ContactDetailsRepository
) : ViewModel() {
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
     * A contact name that can be shown on the screen.
     * This is private to avoid exposing this value to observers.
     */
    private val _contactName = MutableLiveData<String>()
    /**
     * A contact name that can be shown on the screen.
     * Views should use this to get access to the data.
     */
    val contactName: LiveData<String>
        get() = _contactName

    /**
     * A list of contacts that can be shown on the screen.
     * This is private to avoid exposing this value to observers.
     */
    private val _contactNumber = MutableLiveData<String>()
    /**
     * A list of contacts that can be shown on the screen.
     * Views should use this to get access to the data.
     */
    val contactNumber: LiveData<String>
        get() = _contactNumber

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

    /**
     * Initialize all properties for binding to the detail fragment.
     */
    init {
        _contactName.value = selectedContact.name

        _contactNumber.value = selectedContact.number

        requestContactsPermissionOrStartLoadingList()
    }

    private fun requestContactsPermissionOrStartLoadingList() {
        _requestStatus.value = true
    }

    fun onRequestPermissionComplete() {
        _requestStatus.value = null
    }

    fun startLoadingLogHistory() {
        viewModelScope.launch {
            repository.getLogHistoryByNumber(selectedContact)
        }
    }

    val logs = repository.logList

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}