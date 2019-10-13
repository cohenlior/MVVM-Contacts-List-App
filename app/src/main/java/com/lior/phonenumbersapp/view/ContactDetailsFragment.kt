package com.lior.phonenumbersapp.view

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar

import com.lior.phonenumbersapp.R
import com.lior.phonenumbersapp.databinding.FragmentContactDetailsBinding
import com.lior.phonenumbersapp.model.Contact
import com.lior.phonenumbersapp.model.ContactDetailsRepository
import com.lior.phonenumbersapp.model.ContactRepository
import com.lior.phonenumbersapp.viewmodel.ContactDetailsViewModel
import com.lior.phonenumbersapp.viewmodel.ContactDetailsViewModelFactory
import com.lior.phonenumbersapp.viewmodel.ContactListViewModel
import com.lior.phonenumbersapp.viewmodel.ContactListViewModelFactory

private const val ARG_ITEM_CONTACT = "param1"

private const val CALL_LOG_PERMISSION_REQUEST = 2

private const val CALL_LOG_PERMISSION = "android.permission.READ_CALL_LOG"

/**
 * Show contact details on screen.
 */
class ContactDetailsFragment : Fragment() {

    private var param1: Contact? = null

    private lateinit var viewModelFactory: ContactDetailsViewModelFactory

    private lateinit var contactDetailsViewModel: ContactDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getParcelable(ARG_ITEM_CONTACT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContactDetailsBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application

        val repository = ContactDetailsRepository(application)

        viewModelFactory = param1?.let { ContactDetailsViewModelFactory(it, repository) }!!

        contactDetailsViewModel = this.activity?.run {
            ViewModelProviders.of(
                this@ContactDetailsFragment,
                viewModelFactory
            ).get(ContactDetailsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding.viewModel = contactDetailsViewModel

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        contactDetailsViewModel.requestStatus.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                requestCallLogPermissionOrStartLoadingLogs()

                contactDetailsViewModel.onRequestPermissionComplete()
            }
        })

        val adapter = ContactDetailAdapter()

        binding.callLogList.adapter = adapter

        return binding.root
    }

    /**
     * If we don't have permission ask for it and wait until the user grants it
     */
    private fun requestCallLogPermissionOrStartLoadingLogs() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                CALL_LOG_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestCallLogPermission()
            return
        }
        contactDetailsViewModel.startLoadingLogHistory()
    }

    /**
     * Show the user a dialog asking for permission to show call log history.
     */
    private fun requestCallLogPermission() {
        requestPermissions(arrayOf(CALL_LOG_PERMISSION), CALL_LOG_PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALL_LOG_PERMISSION_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactDetailsViewModel.startLoadingLogHistory()
                } else {
                    Toast.makeText(
                        activity, getString(R.string.require_call_log_permission),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment ContactDetailsFragment.
         */
        @JvmStatic
        fun newInstance(param1: Contact) =
            ContactDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ITEM_CONTACT, param1)
                }
            }
    }
}
