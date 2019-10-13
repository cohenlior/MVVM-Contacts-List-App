package com.lior.phonenumbersapp.model
/**
 * Call object is plain Kotlin data class that represent the call history log. This is the
 * object that should be displayed on screen, or manipulated by the app.
 */
data class Call(
    val id: String,
    val type: Int,
    val typeString: String,
    val date: Long
)
