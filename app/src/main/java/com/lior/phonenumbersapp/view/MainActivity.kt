package com.lior.phonenumbersapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lior.phonenumbersapp.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * This is a single activity application. Content is displayed
 * by Fragments.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val listFragment = ContactListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, listFragment)
                .commit()
        }
    }
}
