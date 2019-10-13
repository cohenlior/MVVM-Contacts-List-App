package com.lior.phonenumbersapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
/**
 * Contact object is plain Kotlin data class that represent the contact data. This is the
 * object that should be displayed on screen, or manipulated by the app.
 */
@Parcelize
data class Contact(
    val id: String,
    val name: String,
    val number: String,
    val imageUri: String?
) : Parcelable