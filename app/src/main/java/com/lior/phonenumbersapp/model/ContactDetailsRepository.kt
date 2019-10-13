package com.lior.phonenumbersapp.model

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import android.provider.CallLog.Calls.INCOMING_TYPE
import android.provider.CallLog.Calls.OUTGOING_TYPE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lior.phonenumbersapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * The ContactDetailsRepository simplify the job of the consumer ContactDetailsViewModel
 * by handling the search operation.
 */
class ContactDetailsRepository(private val context: Context) {
    /**
     * A list of contacts that can be shown on the screen.
     * This is private to avoid exposing this value to observers.
     */
    private val _logList = MutableLiveData<List<Call>>()
    /**
     * A list of contacts that can be shown on the screen.
     * Views should use this to get access to the data.
     */
    val logList: LiveData<List<Call>>
        get() = _logList

    /**
     * Get all log history for the selected contact.
     */
    @SuppressLint("MissingPermission")
    suspend fun getLogHistoryByNumber(contact: Contact) {
        withContext(Dispatchers.IO) {

            val logs = mutableListOf<Call>()

            val managedCursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI, null,
                null, null, null
            )
            if (managedCursor?.count ?: 0 > 0) {
                val indexOfName = managedCursor?.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val indexOfType = managedCursor?.getColumnIndex(CallLog.Calls.TYPE)
                val indexOfDate = managedCursor?.getColumnIndex(CallLog.Calls.DATE)

                while (managedCursor!!.moveToNext()) {
                    val name = managedCursor.getString(indexOfName!!)
                    if (name == contact.name) {

                        val callType = Integer.parseInt(managedCursor.getString(indexOfType!!))
                        val callDate = managedCursor.getLong(indexOfDate!!)

                        val typeString = when (callType) {
                            OUTGOING_TYPE -> context.getString(R.string.out_call)
                            INCOMING_TYPE -> context.getString(R.string.incoming_call)
                            else -> null
                        }

                        typeString?.let {
                            val log = Call(contact.id, callType, typeString, callDate)
                            logs.add(log)
                        }
                    }
                }
                managedCursor.close()
                _logList.postValue(logs)
            }
        }
    }
}