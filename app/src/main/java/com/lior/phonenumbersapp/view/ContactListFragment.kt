package com.lior.phonenumbersapp.view

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.lior.phonenumbersapp.databinding.FragmentContactListBinding
import com.lior.phonenumbersapp.model.Contact
import com.lior.phonenumbersapp.model.ContactRepository
import com.lior.phonenumbersapp.viewmodel.ContactListViewModel
import com.lior.phonenumbersapp.viewmodel.ContactListViewModelFactory
import android.provider.ContactsContract
import android.content.Intent
import androidx.lifecycle.Observer

private const val CONTACTS_PERMISSION_REQUEST = 1

private const val CONTACTS_PERMISSION = "android.permission.READ_CONTACTS"

/**
 * Show a list of contacts on screen.
 */
class ContactListFragment : Fragment() {

    private lateinit var viewModelFactory: ContactListViewModelFactory

    private val contactsViewModel: ContactListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        val repository = ContactRepository(activity)

        viewModelFactory = ContactListViewModelFactory(repository)
        ViewModelProviders.of(activity, viewModelFactory).get(ContactListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentContactListBinding.inflate(inflater)

        binding.viewModel = contactsViewModel
        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        contactsViewModel.requestStatus.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                requestContactsPermissionOrStartLoadingList()

                contactsViewModel.onRequestPermissionComplete()
            }
        })

        val adapter = ContactListAdapter(object : ContactListAdapter.OnClickListener {
            override fun onClickContact(contact: Contact) {
                replaceFragment(contact)
            }

            override fun onClickImage(contact: Contact) {
                val intent = Intent(Intent.ACTION_VIEW)
                val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contact.id)
                intent.data = uri
                startActivity(intent)
            }
        })

        binding.contactList.adapter = adapter

        return binding.root
    }

    private fun replaceFragment(contact: Contact) {
        activity!!.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(com.lior.phonenumbersapp.R.id.fragment_container, ContactDetailsFragment.newInstance(contact))
            .addToBackStack(null)
            .commit()
    }

    /**
     * If we don't have permission ask for it and wait until the user grants it
     */
    private fun requestContactsPermissionOrStartLoadingList() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                CONTACTS_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestContactSPermission()
            return
        }
        contactsViewModel.startLoadingContacts()
    }

    /**
     * Show the user a dialog asking for permission to show contacts.
     */
    private fun requestContactSPermission() {
        requestPermissions(arrayOf(CONTACTS_PERMISSION), CONTACTS_PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONTACTS_PERMISSION_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactsViewModel.startLoadingContacts()
                } else {
                    Toast.makeText(
                        activity, getString(com.lior.phonenumbersapp.R.string.require_contacts_permission),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }
}
