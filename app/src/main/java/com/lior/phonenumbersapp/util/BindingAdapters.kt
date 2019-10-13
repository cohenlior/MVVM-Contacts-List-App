package com.lior.phonenumbersapp.util

import android.provider.CallLog.Calls.INCOMING_TYPE
import android.provider.CallLog.Calls.OUTGOING_TYPE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lior.phonenumbersapp.R
import com.lior.phonenumbersapp.model.Call
import com.lior.phonenumbersapp.model.Contact
import com.lior.phonenumbersapp.view.ContactDetailAdapter
import com.lior.phonenumbersapp.view.ContactListAdapter

/**
 * Binding adapter used for setting up the recyclerView adapter and passing contact list data for showing on screen.
 */
@BindingAdapter("contactData")
fun bindRecyclerViewContacts(recyclerView: RecyclerView, data: List<Contact>?) {
    val adapter = recyclerView.adapter as ContactListAdapter
    recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL))
    adapter.submitList(data)
}

/**
 * Binding adapter used for displaying contact images from URI using Glide.
 * If null we will show default image.
 */
@BindingAdapter("photoUri")
fun bindImage(imgView: ImageView, imgUri: String?) {
    if (imgUri != null) {
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions.circleCropTransform())
            .into(imgView)
    } else {
        imgView.setImageResource(R.drawable.ic_image_face_black_24dp)
    }
}

/**
 * Binding adapter used for setting up the recyclerView adapter and passing call log history data for displaying on screen.
 */
@BindingAdapter("logData")
fun bindRecyclerViewLogs(recyclerView: RecyclerView, data: List<Call>?) {
    val adapter = recyclerView.adapter as ContactDetailAdapter
    recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL))
    adapter.submitList(data)
}

/**
 * Binding adapter used for displaying call history log on screen.
 */
@BindingAdapter("imageType")
fun bindImageType(imgView: ImageView, imgType: Int?) {
    when (imgType) {
        OUTGOING_TYPE -> {
            imgView.setImageResource(R.drawable.ic_call_made_black_24dp)
        }
        INCOMING_TYPE -> {
            imgView.setImageResource(R.drawable.ic_call_received_black_24dp)
        }
    }
}

/**
 * Binding adapter used for displaying formatted date on screen.
 */
@BindingAdapter("callDate")
fun TextView.setFormattedDate(data: Long?) {
    data?.let {
        text = convertLongToDateString(data)
    }
}