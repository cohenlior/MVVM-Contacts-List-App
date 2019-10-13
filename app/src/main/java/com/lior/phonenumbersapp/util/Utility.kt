package com.lior.phonenumbersapp.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Formatted date string for showing in the UI.
 */
fun convertLongToDateString(date: Long): String {
    return SimpleDateFormat("dd-MMM', 'HH:mm", Locale.getDefault())
        .format(date).toString()
}