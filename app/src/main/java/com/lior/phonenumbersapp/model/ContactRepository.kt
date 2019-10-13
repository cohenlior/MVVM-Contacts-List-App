package com.lior.phonenumbersapp.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.provider.ContactsContract
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * The ContactRepository simplify the job of the consumer ContactListViewModel
 * by handling the search operation.
 */
class ContactRepository(private val context: Context) {

    /**
     * A list of contacts that can be shown on the screen.
     * This is private to avoid exposing this value to observers.
     */
    private val _contactList = MutableLiveData<List<Contact>>()
    /**
     * A list of contacts that can be shown on the screen.
     * Views should use this to get access to the data.
     */
    val contactList: LiveData<List<Contact>>
        get() = _contactList

    /**
     * get all device contacts sorted by display name.
     */
    suspend fun getContacts() {
        withContext(Dispatchers.IO) {

            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            )

            val contacts = mutableListOf<Contact>()

            val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC"
            )

            if (cursor?.count ?: 0 > 0) {

                val normalizedNumbersAlreadyFound = hashSetOf<String>()
                val indexOfNormalizedNumber =
                    cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
                val indexOfDisplayName = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)
                val indexOfDisplayNumber = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val indexOfPhotoUri = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                val indexOfContactID = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

                while (cursor!!.moveToNext()) {
                    val normalizedNumber = cursor.getString(indexOfNormalizedNumber!!)
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        val name = cursor.getString(indexOfDisplayName!!)
                        val phoneNumber =
                            cursor.getString(indexOfDisplayNumber!!)
                        val photoUri =
                            cursor.getString(indexOfPhotoUri!!)
                        val id =
                            cursor.getString(indexOfContactID!!)

                        Log.d("contact", "getAllContacts: $name $phoneNumber $photoUri")
                        val contact = Contact(id, name, phoneNumber, photoUri)

                        contacts.add(contact)
                    }
                }
            }
            cursor?.close()

            _contactList.postValue(contacts)
        }
    }
}
